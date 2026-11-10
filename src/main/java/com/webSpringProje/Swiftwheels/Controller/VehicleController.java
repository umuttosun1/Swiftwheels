package com.webSpringProje.Swiftwheels.Controller;

import com.webSpringProje.Swiftwheels.DTO.VehicleForm;
import com.webSpringProje.Swiftwheels.Entity.Vehicle;
import com.webSpringProje.Swiftwheels.Entity.VehicleType;
import com.webSpringProje.Swiftwheels.Services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;

/**
 * Araba, jeep ve motor sayfaları tek controller'da: /cars, /jeeps, /motors.
 * (Eskiden aynı kodun üç kopyası vardı.)
 */
@Controller
public class VehicleController {

    private final VehicleService vehicles;

    public VehicleController(VehicleService vehicles) {
        this.vehicles = vehicles;
    }

    private static VehicleType type(String path) {
        return VehicleType.fromPath(path).orElseThrow(() -> new NoSuchElementException("Sayfa bulunamadı"));
    }

    @GetMapping("/{type:cars|jeeps|motors}")
    public String list(@PathVariable String type, Model model) {
        VehicleType t = type(type);
        model.addAttribute("type", t);
        model.addAttribute("vehicles", vehicles.list(t));
        return "vehicle_list";
    }

    @GetMapping("/{type:cars|jeeps|motors}/{id:\\d+}")
    public String detail(@PathVariable String type, @PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicles.get(type(type), id));
        return "vehicle_detail";
    }

    /** Resimler artık sayfaya base64 gömülmüyor; tarayıcı ayrı ve önbelleklenebilir olarak indiriyor. */
    @GetMapping("/vehicles/{id}/image")
    @ResponseBody
    public ResponseEntity<byte[]> image(@PathVariable Long id) {
        Vehicle v = vehicles.get(id);
        if (!v.hasImage()) {
            return ResponseEntity.notFound().build();
        }
        MediaType mediaType = v.getImageContentType() != null
                ? MediaType.parseMediaType(v.getImageContentType())
                : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(Duration.ofHours(1)))
                .body(v.getImage());
    }

    /* ---------------- admin: ekle / düzenle / sil ---------------- */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{type:cars|jeeps|motors}/new")
    public String newForm(@PathVariable String type, Model model) {
        model.addAttribute("type", type(type));
        model.addAttribute("vehicleForm", new VehicleForm());
        return "vehicle_form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{type:cars|jeeps|motors}")
    public String create(@PathVariable String type, @Valid @ModelAttribute("vehicleForm") VehicleForm form,
                         BindingResult errors, Model model, RedirectAttributes ra) throws IOException {
        VehicleType t = type(type);
        vehicles.validate(t, form, errors);
        if (errors.hasErrors()) {
            model.addAttribute("type", t);
            return "vehicle_form";
        }
        Vehicle saved = vehicles.create(t, form);
        ra.addFlashAttribute("success", saved.getTitle() + " eklendi.");
        return "redirect:/" + t.getPath() + "/" + saved.getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{type:cars|jeeps|motors}/{id:\\d+}/edit")
    public String editForm(@PathVariable String type, @PathVariable Long id, Model model) {
        Vehicle v = vehicles.get(type(type), id);
        model.addAttribute("type", v.getVehicleType());
        model.addAttribute("vehicle", v);
        model.addAttribute("vehicleForm", vehicles.toForm(v));
        return "vehicle_form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{type:cars|jeeps|motors}/{id:\\d+}")
    public String update(@PathVariable String type, @PathVariable Long id,
                         @Valid @ModelAttribute("vehicleForm") VehicleForm form, BindingResult errors,
                         Model model, RedirectAttributes ra) throws IOException {
        Vehicle v = vehicles.get(type(type), id);
        vehicles.validate(v.getVehicleType(), form, errors);
        if (errors.hasErrors()) {
            model.addAttribute("type", v.getVehicleType());
            model.addAttribute("vehicle", v);
            return "vehicle_form";
        }
        vehicles.update(id, form);
        ra.addFlashAttribute("success", "Araç güncellendi.");
        return "redirect:/" + v.getVehicleType().getPath() + "/" + id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{type:cars|jeeps|motors}/{id:\\d+}/delete")
    public String delete(@PathVariable String type, @PathVariable Long id, RedirectAttributes ra) {
        VehicleType t = type(type);
        Vehicle v = vehicles.get(t, id);
        try {
            vehicles.delete(id);
            ra.addFlashAttribute("success", v.getTitle() + " silindi.");
            return "redirect:/" + t.getPath();
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/" + t.getPath() + "/" + id;
        }
    }
}
