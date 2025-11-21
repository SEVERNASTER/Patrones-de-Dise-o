# Sistema de Pedidos de Restaurante

## Tecnologias Utilizadas

- **Lenguaje:** Java 8+
- **Paradigma:** Programacion Orientada a Objetos
- **Herramientas:** JDK (Java Development Kit)

---

## Patrones de Diseno Implementados

El sistema implementa tres patrones de diseno:

| Tipo | Patron | Clases Involucradas |
|------|--------|---------------------|
| Creacional | Factory Method | ProductoFactory, BebidaFactory, PlatoFactory |
| Estructural | Decorator | ProductoDecorator, ExtraQueso, ExtraTocino |
| Comportamiento | Observer | Observador, Cocina, Caja, Pedido |

---

## Explicacion del Caso de Uso

El sistema simula la gestion de pedidos en un restaurante. El flujo principal es:

1. **Creacion de productos:** El sistema permite crear diferentes tipos de productos del menu (bebidas y platos) mediante fabricas especializadas.

2. **Personalizacion de productos:** Los clientes pueden agregar extras a sus productos (queso adicional, tocino, etc.) sin modificar el producto base.

3. **Gestion de pedidos:** Cada pedido tiene un estado (nuevo, en preparacion, listo, entregado) y cuando este cambia, se notifica automaticamente a los sistemas interesados (cocina y caja).

### Flujo del Sistema

```
Cliente solicita producto
        |
        v
Factory crea el producto base
        |
        v
Decorator anade extras (opcional)
        |
        v
Producto se agrega al Pedido
        |
        v
Pedido cambia de estado
        |
        v
Observer notifica a Cocina y Caja
```

---

## Razon de Cada Patron de Diseno

### 1. Factory Method (Patron Creacional)

**Problema que resuelve:**
El restaurante tiene diferentes categorias de productos (bebidas, platos fuertes, postres). Sin este patron, el codigo cliente tendria que conocer todas las clases concretas y usar multiples condicionales para crear cada tipo de producto.

**Razon de uso:**
- Centraliza la logica de creacion de productos en fabricas especializadas.
- El codigo cliente solo conoce la interfaz `Producto`, no las clases concretas.
- Facilita agregar nuevas categorias de productos sin modificar el codigo existente.
- Cumple con el principio Open/Closed: abierto para extension, cerrado para modificacion.

**Ejemplo en el sistema:**
```java
// Sin Factory Method (problematico)
Producto p;
if (tipo.equals("bebida")) {
    p = new Bebida("Coca", 3.00);
} else if (tipo.equals("plato")) {
    p = new PlatoFuerte("Pizza", 15.00);
}

// Con Factory Method (solucion)
ProductoFactory fabrica = new BebidaFactory();
Producto p = fabrica.crearProducto("COCA");
```

### 2. Decorator (Patron Estructural)

**Problema que resuelve:**
Los clientes pueden personalizar sus productos con extras (queso, tocino, salsas). Sin este patron, seria necesario crear una clase para cada combinacion posible (HamburguesaConQueso, HamburguesaConTocino, HamburguesaConQuesoYTocino, etc.), resultando en una explosion de clases.

**Razon de uso:**
- Permite anadir funcionalidad a objetos de forma dinamica en tiempo de ejecucion.
- Evita la explosion combinatoria de subclases.
- Los decoradores se pueden apilar para combinar multiples extras.
- No modifica las clases originales de productos.

**Ejemplo en el sistema:**
```java
// Producto base
Producto hamburguesa = fabrica.crearProducto("HAMBURGUESA"); // $12.00

// Aplicar decoradores (extras)
Producto conQueso = new ExtraQueso(hamburguesa);           // $14.00
Producto completa = new ExtraTocino(conQueso);             // $17.00

// Los decoradores se pueden encadenar
Producto hamburguesaCompleta = new ExtraTocino(new ExtraQueso(hamburguesa));
```

### 3. Observer (Patron de Comportamiento)

**Problema que resuelve:**
Cuando un pedido cambia de estado, multiples sistemas necesitan ser informados (cocina para preparar, caja para facturar, meseros para servir). Sin este patron, el pedido tendria que conocer y llamar directamente a cada sistema, creando un alto acoplamiento.

**Razon de uso:**
- Desacopla el pedido de los sistemas que dependen de el.
- Permite agregar o quitar observadores sin modificar la clase Pedido.
- Garantiza que todos los sistemas interesados reciban la notificacion automaticamente.
- Facilita la escalabilidad: se pueden agregar nuevos observadores (sistema de delivery, notificaciones al cliente, etc.).

**Ejemplo en el sistema:**
```java
// Crear pedido y registrar observadores
Pedido pedido = new Pedido();
pedido.agregarObservador(new Cocina());
pedido.agregarObservador(new Caja());

// Al cambiar estado, todos los observadores son notificados automaticamente
pedido.cambiarEstado("EN PREPARACION");
// Salida:
//   [COCINA] Pedido cambio a: EN PREPARACION
//   [CAJA] Pedido cambio a: EN PREPARACION
```

---

## Diagrama de Clases Simplificado

```
                    <<interface>>
                      Producto
                    +getNombre()
                    +getPrecio()
                         ^
                         |
         +---------------+---------------+
         |               |               |
      Bebida       PlatoFuerte    ProductoDecorator
                                         ^
                                         |
                                 +-------+-------+
                                 |               |
                            ExtraQueso     ExtraTocino


    <<abstract>>                      <<interface>>
  ProductoFactory                      Observador
 +crearProducto()                     +notificar()
         ^                                  ^
         |                                  |
    +----+----+                      +------+------+
    |         |                      |             |
BebidaFactory PlatoFactory        Cocina         Caja


                    Pedido
              -productos: List
              -observadores: List
              -estado: String
              +agregarObservador()
              +cambiarEstado()
              +notificarTodos()
```

---

## Instrucciones de Ejecucion

### Compilar
```bash
javac SistemaRestaurante.java
```

### Ejecutar
```bash
java SistemaRestaurante
```

### Salida Esperada
```
=== SISTEMA DE RESTAURANTE ===

1. FACTORY METHOD - Creando productos:
   Creado: Coca-Cola - $3.0
   Creado: Hamburguesa - $12.0

2. DECORATOR - Anadiendo extras:
   Original: Hamburguesa - $12.0
   Decorada: Hamburguesa + Queso + Tocino - $17.0

3. OBSERVER - Notificando cambios:
  [COCINA] Pedido cambio a: EN PREPARACION
  [CAJA] Pedido cambio a: EN PREPARACION
  [COCINA] Pedido cambio a: LISTO
  [CAJA] Pedido cambio a: LISTO
  [COCINA] Pedido cambio a: ENTREGADO
  [CAJA] Pedido cambio a: ENTREGADO

--- PEDIDO (ENTREGADO) ---
  Hamburguesa + Queso + Tocino $17.00
  Coca-Cola                    $3.00
  TOTAL:                       $20.00

=== FIN DE DEMOSTRACION ===
```

---

## Conclusiones

La implementacion de estos tres patrones de diseno permite:

1. **Mantenibilidad:** El codigo esta organizado y es facil de entender.
2. **Escalabilidad:** Se pueden agregar nuevos productos, extras u observadores sin modificar el codigo existente.
3. **Bajo acoplamiento:** Las clases tienen responsabilidades bien definidas y minimas dependencias entre si.
4. **Reutilizacion:** Los patrones proporcionan soluciones probadas que se pueden aplicar en otros contextos similares.