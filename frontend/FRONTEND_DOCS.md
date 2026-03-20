# Documentación del Frontend - Ecommerce Inventory

## Estructura General

```
frontend/src/app/
├── core/                           # Configuraciones centrales
│   ├── auth-guard.ts              # Guardia de autenticación
│   ├── auth-interceptor.ts        # Interceptor para JWT
│   └── Environment.ts             # Variables de entorno
├── modules/
│   ├── login/                     # Módulo de autenticación
│   ├── main/                      # Layout principal (sidebar + navbar)
│   │   ├── main.ts               # Componente raíz del layout
│   │   ├── main.html
│   │   └── main.routes.ts        # Rutas del layout
│   ├── dashboard/                 # Dashboard con métricas
│   └── products/                  # Módulo de productos (tabs)
│       ├── products.ts            # Componente contenedor con tabs
│       ├── products.html
│       ├── products.routes.ts     # Rutas simplificadas
│       ├── product-list/          # Lista de productos
│       ├── product-form/          # Formulario create/edit
│       ├── product.service.ts     # Servicio de productos
│       └── product.model.ts       # Interfaces y tipos
├── shared/
│   ├── navbar/                   # Barra de navegación
│   │   ├── navbar.ts
│   │   ├── navbar.html
│   │   └── navbar.css
│   └── sidenav/                  # Sidebar navigation
│       ├── sidenav.ts
│       ├── sidenav.html
│       └── sidenav.css
└── app.routes.ts                 # Rutas principales
```

---

## Componentes Principales

### 1. Layout Principal (`main`)

#### `main.ts`
- Controla el estado del sidebar (collapse/expand)
- Define el menú de navegación
- Pasa datos al sidebar via inputs
- **Cierra el sidebar con la tecla Escape** (`@HostListener('window:keydown.escape')`)

#### `main.html`
- Estructura flexbox: sidebar + contenido principal
- Efecto overlay cuando el sidebar está abierto:
  - `opacity-50` - opacidad reducida
  - `pointer-events-none` - sin interacción
- Transición suave de 300ms

#### Rutas del menú
```typescript
menuItems = [
  { label: 'Dashboard', icon: 'dashboard', route: '/main/dashboard' },
  { label: 'Productos', icon: 'inventory', route: '/main/products' },
  { label: 'Categorías', icon: 'category', route: '/main/categories' },
  { label: 'Proveedores', icon: 'local_shipping', route: '/main/suppliers' },
  { label: 'Usuarios', icon: 'people', route: '/main/users' },
  { label: 'Reportes', icon: 'assessment', route: '/main/reports' },
  { label: 'Configuración', icon: 'settings', route: '/main/settings' },
]
```

---

### 2. Sidebar (`sidenav`)

#### Características
- **Overlay**: funciona como modal al abrirse
- **Fixed position**: siempre visible al hacer scroll
- **Colapsible**: se oculta completamente cuando está collapsed
- **Z-index**: 40 (debajo del navbar que tiene 50)

#### Inputs
- `collapsed: InputSignal<boolean>` - estado de collapse
- `menuItems: InputSignal<MenuItem[]>` - items del menú

#### Outputs
- `closedSidenav: output<void>()` - emite cuando se cierra

#### Plantilla
- Logo marca "Techman" con icono
- Lista de navegación con iconos Material Symbols
- Resaltado de ruta activa (`routerLinkActive`)
- Botón de cerrar sesión
- Tooltip en items cuando está colapsado

---

### 3. Navbar

#### Características
- **Z-index**: 50 (encima del sidebar)
- Título dinámico de la página
- Fecha actual formateada
- Botón de notificaciones con badge
- Menú desplegable de usuario

#### Comportamientos
- Botón hamburguesa para abrir/cerrar sidebar
- Dropdown con opciones: Perfil, Configuración, Cerrar sesión

---

### 4. Módulo Productos (`products`)

#### Sistema de Tabs

**Componente contenedor** (`products.ts`)
```typescript
activeTab: 'list' | 'form' = 'list'
editingProductId: number | null = null

// Métodos
showNewForm()    // Abre formulario vacío
editProduct(id)  // Abre formulario con datos
onSaved()        // Regresa a lista después de guardar
```

**Template** (`products.html`)
- Card con estilo consistente (similar al login)
- Responsiva: `p-4` (móvil) → `p-6` (tablet) → `p-8` (escritorio)
- Ancho máximo: 100% → 768px → 1024px
- Tabs visuales: "Lista" / "Nuevo o Editar"
- Botón "+ Nuevo" que abre el formulario

**Rutas simplificadas**
```typescript
// Antes (múltiples rutas)
path: 'products/product-list'  // Lista
path: 'products/product-form'  // Formulario

// Ahora (una sola ruta)
path: 'products'  // Carga el componente con tabs
```

---

### 5. Dashboard

#### Tarjetas de Métricas
- Ventas Hoy
- Ventas Mes
- Usuarios Registrados
- Stock Total

#### Tabla de Movimientos
- Lista de últimas entradas y salidas
- Color verde para entradas, rojo para salidas
- Formato: Producto | Tipo | Cantidad | Fecha

---

## Autenticación

### Flujo
1. Usuario inicia sesión → `AuthService.login()`
2. Tokens guardados en `sessionStorage`
3. `AuthInterceptor` añade token a cada request
4. `AuthGuard` protege rutas privadas

### Keys de sessionStorage
- `accessToken` - Token de acceso
- `refreshToken` - Token de renovación

---

## Estilos (Tailwind CSS)

### Tema Oscuro
- Background principal: `bg-slate-800`, `bg-slate-900`
- Bordes: `border-slate-700`
- Texto: `text-white`, `text-slate-300`, `text-slate-400`
- Acentos: `text-teal-400`, `bg-teal-500`

### Clases Utilitarias
- `fixed` - Posición fija
- `z-40`, `z-50` - Capas
- `opacity-50` - Opacidad
- `pointer-events-none` - Deshabilitar clicks
- `transition-all duration-300` - Transiciones suaves

---

## Build y Ejecución

```bash
# Desarrollo
cd frontend
npm run start

# Producción
npm run build
```

---

## Notas Importantes

1. **Rutas con prefijo `/main/`**: Todas las rutas del menú tienen el prefijo `/main/` porque el componente Main es un `loadChildren` de la ruta principal.

2. **Sidebar como overlay**: Cuando está abierto, el contenido principal queda con `opacity-50` y sin interacción para indicar que hay un menú abierto.

3. **Tabs en productos**: El componente `Products` maneja todo internamente con signals, sin necesidad de rutas adicionales.

4. **Responsividad**: Los componentes principales usan clases como `p-4 md:p-6 lg:p-8` para adaptarse a diferentes tamaños de pantalla.
