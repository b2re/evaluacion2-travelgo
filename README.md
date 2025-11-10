# 🌍 TravelGo - Evaluación 2

> Proyecto Android desarrollado con **Kotlin + Jetpack Compose**, basado en la arquitectura **MVVM**, que implementa conexión a API REST, manejo de estado, navegación y la integración de cámara/galería para el avatar del usuario.

---

## 🧩 Descripción General

**TravelGo** es una aplicación móvil de tipo SPA (Single Page App) para la gestión de usuarios y paquetes turísticos sustentables.  
El proyecto fue desarrollado como parte de la **Evaluación 2** del ramo de programación móvil, y combina tanto conceptos teóricos de arquitectura como la práctica de integración de servicios externos (API REST) y recursos nativos (cámara, almacenamiento, permisos, etc.).

---

## 🚀 Funcionalidades Implementadas

### ✅ 1. Arquitectura Base
- Estructura **MVVM** separada por capas:
  - `data/` → Capa de datos (Retrofit, DTO, DataStore)
  - `repository/` → Manejo de lógica de acceso a datos
  - `viewmodel/` → Manejo de estados y lógica de presentación
  - `ui/` → Pantallas y componentes Compose
- Navegación entre pantallas con **Navigation Compose**.

### ✅ 2. API REST (según la guía)
Implementación completa siguiendo la [Guía: Cómo Agregar API REST a tu Proyecto Android](./Guia_Agregar_API_REST_A_Tu_Proyecto.md):
- Cliente HTTP configurado con **Retrofit** + **OkHttp**.
- **AuthInterceptor** para inyectar tokens JWT automáticamente.
- **HttpLoggingInterceptor** para visualizar peticiones/respuestas en Logcat.
- DTOs (`UserDto`, `LoginRequest`, `LoginResponse`, etc.) definidos según los endpoints.
- Repositorio (`UserRepository`) que abstrae el acceso a la API.
- Manejo de errores y estados (`isLoading`, `error`, `success`) en `ProfileViewModel`.

### ✅ 3. Pantalla de Perfil (ProfileScreen)
- Obtiene los datos del usuario desde la API y los muestra dinámicamente.
- Muestra estados de carga y error con UI reactiva (Compose + StateFlow).
- Botón de **“Refrescar”** para recargar datos del servidor.

### ✅ 4. Integración de Cámara y Galería
Implementación siguiendo el [Tutorial de Cámara y Avatar](./TUTORIAL_CAMARA_AVATAR.md):
- Permisos dinámicos (`CAMERA`, `READ_MEDIA_IMAGES`, `READ_EXTERNAL_STORAGE`).
- **Accompanist Permissions** para control declarativo de permisos.
- **ActivityResultContracts** para manejar resultados de cámara y galería.
- Uso de **Coil** para mostrar imágenes (URIs locales y remotas).
- Botones funcionales para “Tomar foto” y “Elegir de galería”.
- Estado `avatarUri` agregado al `ProfileUiState`.
- **Persistencia del avatar** implementada con **DataStore Preferences**.

### ✅ 5. Dependencias configuradas correctamente
Incluye todas las librerías necesarias en `build.gradle.kts`:
```kotlin
implementation("io.coil-kt:coil-compose:2.6.0")
implementation("com.google.accompanist:accompanist-permissions:0.35.2-beta")
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
implementation("androidx.datastore:datastore-preferences:1.0.0")
implementation("androidx.navigation:navigation-compose:2.8.3")
```

---

## ⚙️ Estructura del Proyecto

```
com.example.travelgo/
│
├── data/
│   ├── remote/           # API REST (RetrofitClient, ApiService)
│   ├── local/            # DataStore, SessionManager
│   └── dto/              # Clases de transferencia de datos (UserDto, LoginRequest)
│
├── repository/
│   └── UserRepository.kt # Coordinación de datos remotos/locales
│
├── ui/
│   ├── screens/          # Pantallas principales (Home, Profile)
│   ├── components/       # Componentes reutilizables (dialogs, botones)
│   ├── navigation/       # AppNavigation con rutas Compose
│   └── theme/            # Colores y tipografía
│
├── viewmodel/
│   └── ProfileViewModel.kt
│
└── MainActivity.kt       # Punto de entrada de la app
```

---

## 🧠 Conceptos Aprendidos

- Patrón **MVVM** aplicado en proyectos reales.
- Uso de **Retrofit + OkHttp + Coroutines** para peticiones HTTP asíncronas.
- **StateFlow** como fuente única de verdad para los estados de UI.
- **Accompanist Permissions** para permisos en Compose.
- **FileProvider** para URIs seguras entre apps.
- **Coil** para carga eficiente de imágenes.
- **DataStore** para persistencia de preferencias y URIs.
- **ActivityResult API** moderna (reemplazo de `startActivityForResult`).

---

## ⚠️ Aspectos Pendientes / Mejoras Futuras

| Categoría | Descripción |
|------------|--------------|
| 🔐 Autenticación | Implementar login real con JWT y guardar token en `SessionManager`. |
| 🧩 API dinámica | Cambiar base URL (`BASE_URL`) desde DummyJSON a API propia del proyecto. |
| 🖼️ FileProvider | Configurar archivo `file_paths.xml` y `<provider>` en `AndroidManifest.xml` (opcional si se usa `MediaStore`). |
| 💾 Subida de imagen | Agregar endpoint en Retrofit para subir el avatar al servidor. |
| 📱 Navegación completa | Integrar todas las pantallas (Home, Reservas, Paquetes). |
| 🧪 Tests | Agregar Unit Tests y pruebas de integración para `UserRepository` y `ProfileViewModel`. |
| 🎨 Diseño | Mejorar UI/UX con Material3 avanzado (animaciones, layouts responsivos). |

---

## 🧰 Tecnologías y Librerías Usadas

| Tipo | Herramienta |
|------|--------------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose |
| Arquitectura | MVVM |
| Navegación | Navigation Compose |
| Estado | StateFlow + ViewModel |
| Networking | Retrofit + OkHttp + Gson Converter |
| Imágenes | Coil |
| Permisos | Accompanist Permissions |
| Persistencia | DataStore Preferences |
| Logging | HttpLoggingInterceptor |
| IDE | Android Studio Iguana+ |

---

## 🧪 Estado Final

| Módulo | Estado | Descripción |
|--------|--------|-------------|
| MVVM Base | ✅ | Implementado correctamente |
| API REST | ✅ | Funciona con DummyJSON |
| Cámara/Galería | ✅ | Totalmente funcional |
| Persistencia de Avatar | ✅ | DataStore operativa |
| FileProvider | ⚠️ | Parcial, requiere ajuste en manifest |
| Login/Auth | 🚧 | En desarrollo |
| Pruebas | 🚧 | No implementadas aún |

---

## 👥 Integrantes

-👤 Daniel Castro 
-👤 Bruno Ratto

## 👨‍🏫 Profesor : Roberto Arce 




