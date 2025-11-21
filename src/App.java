/**
 * SISTEMA DE PEDIDOS DE RESTAURANTE - Demostración de Patrones de Diseño
 * 
 * Patrones implementados:
 * 1. FACTORY METHOD (Creacional) - Crear productos del menú
 * 2. DECORATOR (Estructural) - Añadir extras a productos
 * 3. OBSERVER (Comportamiento) - Notificar cambios de estado del pedido
 */

import java.util.*;

// PATRÓN FACTORY METHOD (CREACIONAL)
// Propósito: Crear objetos sin especificar la clase concreta exacta.
// Uso: Diferentes fábricas crean diferentes tipos de productos del menú.

interface Producto {
    String getNombre();
    double getPrecio();
}

class Bebida implements Producto {
    private String nombre;
    private double precio;
    
    public Bebida(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

class PlatoFuerte implements Producto {
    private String nombre;
    private double precio;
    
    public PlatoFuerte(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

// Fábrica abstracta - Define el Factory Method
abstract class ProductoFactory {
    public abstract Producto crearProducto(String tipo);
}

// Fábrica concreta de Bebidas
class BebidaFactory extends ProductoFactory {
    public Producto crearProducto(String tipo) {
        switch (tipo) {
            case "COCA": return new Bebida("Coca-Cola", 3.00);
            case "JUGO": return new Bebida("Jugo Natural", 4.00);
            default: return new Bebida("Agua", 2.00);
        }
    }
}

// Fábrica concreta de Platos
class PlatoFactory extends ProductoFactory {
    public Producto crearProducto(String tipo) {
        switch (tipo) {
            case "HAMBURGUESA": return new PlatoFuerte("Hamburguesa", 12.00);
            case "PIZZA": return new PlatoFuerte("Pizza", 15.00);
            default: return new PlatoFuerte("Ensalada", 8.00);
        }
    }
}

// PATRÓN DECORATOR (ESTRUCTURAL)
// Propósito: Añadir funcionalidad a objetos dinámicamente.
// Uso: Agregar extras (queso, tocino) a productos sin modificar la clase base.

// Decorador base
abstract class ProductoDecorator implements Producto {
    protected Producto producto;
    
    public ProductoDecorator(Producto producto) {
        this.producto = producto;
    }
}

// Decorador concreto: Extra Queso
class ExtraQueso extends ProductoDecorator {
    public ExtraQueso(Producto producto) {
        super(producto);
    }
    
    public String getNombre() { return producto.getNombre() + " + Queso"; }
    public double getPrecio() { return producto.getPrecio() + 2.00; }
}

// Decorador concreto: Tocino
class ExtraTocino extends ProductoDecorator {
    public ExtraTocino(Producto producto) {
        super(producto);
    }
    
    public String getNombre() { return producto.getNombre() + " + Tocino"; }
    public double getPrecio() { return producto.getPrecio() + 3.00; }
}

// PATRÓN OBSERVER (COMPORTAMIENTO)
// Propósito: Notificar automáticamente a múltiples objetos cuando hay cambios.
// Uso: Cocina y Caja son notificados cuando el estado del pedido cambia.

// Interfaz Observer
interface Observador {
    void notificar(String mensaje);
}

// Observador concreto: Cocina
class Cocina implements Observador {
    public void notificar(String mensaje) {
        System.out.println("  [COCINA] " + mensaje);
    }
}

// Observador concreto: Caja
class Caja implements Observador {
    public void notificar(String mensaje) {
        System.out.println("  [CAJA] " + mensaje);
    }
}

// Sujeto Observable: Pedido
class Pedido {
    private List<Producto> productos = new ArrayList<>();
    private List<Observador> observadores = new ArrayList<>();
    private String estado = "NUEVO";
    
    public void agregarObservador(Observador o) {
        observadores.add(o);
    }
    
    private void notificarTodos(String mensaje) {
        for (Observador o : observadores) {
            o.notificar(mensaje);
        }
    }
    
    public void agregarProducto(Producto p) {
        productos.add(p);
    }
    
    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        notificarTodos("Pedido cambió a: " + estado);
    }
    
    public double getTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }
    
    public void mostrar() {
        System.out.println("\n--- PEDIDO (" + estado + ") ---");
        for (Producto p : productos) {
            System.out.printf("  %-25s $%.2f%n", p.getNombre(), p.getPrecio());
        }
        System.out.printf("  TOTAL:                    $%.2f%n", getTotal());
    }
}





public class App {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE RESTAURANTE ===\n");
        
        // --- FACTORY METHOD: Crear productos usando fábricas ---
        System.out.println("1. FACTORY METHOD - Creando productos:");
        ProductoFactory fabricaBebidas = new BebidaFactory();
        ProductoFactory fabricaPlatos = new PlatoFactory();
        
        Producto coca = fabricaBebidas.crearProducto("COCA");
        Producto hamburguesa = fabricaPlatos.crearProducto("HAMBURGUESA");
        
        System.out.println("   Creado: " + coca.getNombre() + " - $" + coca.getPrecio());
        System.out.println("   Creado: " + hamburguesa.getNombre() + " - $" + hamburguesa.getPrecio());
        
        // --- DECORATOR: Añadir extras a productos ---
        System.out.println("\n2. DECORATOR - Añadiendo extras:");
        Producto hamburguesaCompleta = new ExtraTocino(new ExtraQueso(hamburguesa));
        
        System.out.println("   Original: " + hamburguesa.getNombre() + " - $" + hamburguesa.getPrecio());
        System.out.println("   Decorada: " + hamburguesaCompleta.getNombre() + " - $" + hamburguesaCompleta.getPrecio());
        
        // --- OBSERVER: Notificar cambios de estado ---
        System.out.println("\n3. OBSERVER - Notificando cambios:");
        Pedido pedido = new Pedido();
        pedido.agregarObservador(new Cocina());
        pedido.agregarObservador(new Caja());
        
        pedido.agregarProducto(hamburguesaCompleta);
        pedido.agregarProducto(coca);
        
        pedido.cambiarEstado("EN PREPARACIÓN");
        pedido.cambiarEstado("LISTO");
        pedido.cambiarEstado("ENTREGADO");
        
        pedido.mostrar();        
    }
}