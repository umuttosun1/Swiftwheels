# SwiftWheels — araç kiralama (Spring Boot)

Araba, jeep ve motor kiralama web uygulaması: vitrin ve arama, kayıt/giriş,
tarih seçerek kiralama, profilden iade; admin için araç yönetimi, kiralamalar,
kullanıcılar ve ciro paneli.

**Teknolojiler:** Java 17+, Spring Boot 3.4 (Web, Security, Data JPA, Validation),
Thymeleaf, Bootstrap 5, PostgreSQL (varsayılan) ya da MySQL.

## Çalıştırma

1. **Veritabanı** — PostgreSQL'de kullanıcıyı ve veritabanını oluşturun:
   ```powershell
   powershell -ExecutionPolicy Bypass -File scripts\setup-database.ps1
   ```
   (Tablolar ilk açılışta otomatik oluşur.)
2. **Uygulama**
   ```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
   ```
3. <http://localhost:8080> adresini açın.

İlk açılışta bir admin hesabı ve örnek araçlar eklenir:

| Hesap | Şifre |
|---|---|
| `admin@swiftwheels.com` | `Admin123!` *(yalnızca yerel deneme için — `ADMIN_PASSWORD` ile değiştirin)* |

### Ayarlar (ortam değişkenleri)

Şifreler koda yazılmaz; hepsi ortam değişkeniyle verilir.

| Değişken | Varsayılan |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/swiftwheels_web` |
| `DB_USER` / `DB_PASSWORD` | `swiftwheels` / `swiftwheels` |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | `admin@swiftwheels.com` / `Admin123!` |
| `SEED_DEMO_DATA` | `true` (veritabanında hiç araç yoksa örnekleri ekler) |
| `PORT` | `8080` |

**MySQL** ile: `SPRING_PROFILES_ACTIVE=mysql` (ve `DB_URL`, `DB_USER`, `DB_PASSWORD`).

## Testler

```bash
./mvnw test
```
Testler bellek içi H2 veritabanıyla çalışır, kurulum gerekmez: yetkilendirme
(kullanıcı araç ekleyemez, admin ekleyebilir, CSRF), kayıt (aynı e-posta,
doğrulama, BCrypt) ve kiralama kuralları (stok, iade, ciro, tarih ve yaş).

## Özellikler

- **Kiralama:** tarih aralığı (en fazla 60 gün), teslimat adresi, isteğe bağlı
  kasko + sigorta paketi, sözleşme onayı; ücret = günlük fiyat × gün + paket.
  Stok satır kilidiyle düşer (son aracı iki kişi aynı anda kiralayamaz),
  18 yaş kontrolü yapılır. Profilden iade edilir; kayıt silinmez, ciro korunur.
- **Admin paneli:** araç / stok / kiradaki / gecikmedeki araç sayısı ve toplam
  ciro; kiralamalar (iade alma), araçlar (ekle, düzenle, sil, fotoğraf) ve
  kullanıcılar (admin yetkisi verme/geri alma).
- **Güvenlik:** araç ekleme/düzenleme/silme ve panel yalnızca admin; çıkış CSRF
  korumalı POST; benzersiz e-posta; sunucu tarafı form doğrulaması.
- Vitrin ve araç sayfaları herkese açık; arama marka, model ve renkte çalışır.

## Proje yapısı

```
Config/       SecurityConfig, AppConfig (PasswordEncoder, Clock), DataSeeder, giriş yönlendirme
Controller/   VehicleController (/cars, /jeeps, /motors), RentalController, AdminController, AuthController
DTO/          VehicleForm, RentForm, UsersDto (doğrulama kuralları)
Entity/       Vehicle (+ Car, Jeep, Motor), VehicleType, Rental, Users
Repos/        Spring Data repository'leri
Services/     VehicleService, RentalService, UserService
templates/    fragments/layout.html (ortak menü, kart), sayfa şablonları
```
