package pe.edu.upeu.sysventas.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.SessionManager;
import pe.edu.upeu.sysventas.enums.TipoPago;
import pe.edu.upeu.sysventas.model.CarritoVenta;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.model.Funcion;
import pe.edu.upeu.sysventas.model.ItemCarrito;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.model.Venta;
import pe.edu.upeu.sysventas.model.VentaDetalle;
import pe.edu.upeu.sysventas.service.IClienteService;
import pe.edu.upeu.sysventas.service.IFuncionService;
import pe.edu.upeu.sysventas.service.IProductoService;
import pe.edu.upeu.sysventas.service.IUsuarioService;
import pe.edu.upeu.sysventas.service.IVentaService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class VentaController {

    private final IClienteService clienteService;
    private final IFuncionService funcionService;
    private final IProductoService productoService;
    private final IVentaService ventaService;
    private final IUsuarioService usuarioService;

    @FXML
    private AnchorPane miContenedor;
    @FXML
    private ComboBox<Cliente> cbCliente;
    @FXML
    private ComboBox<Funcion> cbFuncion;
    @FXML
    private TextField txtEntradas;
    @FXML
    private ComboBox<TipoPago> cbTipoPago;
    @FXML
    private ComboBox<Producto> cbProducto;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TableView<ItemCarrito> tablaCarrito;
    @FXML
    private TableView<Venta> tablaVentas;
    @FXML
    private Label lblSubEntradas;
    @FXML
    private Label lblSubProductos;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblMsg;

    private final CarritoVenta carrito = new CarritoVenta();

    @FXML
    public void initialize() {
        cbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente c) {
                return c == null ? "" : c.getDniruc() + " - " + c.getNombres();
            }

            @Override
            public Cliente fromString(String texto) {
                return null;
            }
        });
        cbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion f) {
                return f == null ? "" : f.getEtiqueta();
            }

            @Override
            public Funcion fromString(String texto) {
                return null;
            }
        });
        cbProducto.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto p) {
                return p == null ? "" : p.getNombre() + " - S/ " + formato(p.getPrecio());
            }

            @Override
            public Producto fromString(String texto) {
                return null;
            }
        });

        cbCliente.setOnShowing(e -> refrescarCombo(cbCliente, clienteService.findAll()));
        cbFuncion.setOnShowing(e -> refrescarCombo(cbFuncion, funcionService.findAll()));
        cbProducto.setOnShowing(e -> refrescarCombo(cbProducto, productoService.findAll()));
        refrescarCombo(cbCliente, clienteService.findAll());
        refrescarCombo(cbFuncion, funcionService.findAll());
        refrescarCombo(cbProducto, productoService.findAll());

        cbTipoPago.setItems(FXCollections.observableArrayList(TipoPago.values()));

        txtEntradas.setText("1");
        txtCantidad.setText("1");
        txtEntradas.textProperty().addListener((obs, anterior, nuevo) -> actualizarTotales());
        cbFuncion.valueProperty().addListener((obs, anterior, nuevo) -> actualizarTotales());

        configurarTablaCarrito();
        configurarTablaVentas();
        refrescarCarrito();
        refrescarVentas();
    }

    private <T> void refrescarCombo(ComboBox<T> combo, List<T> datos) {
        T actual = combo.getValue();
        combo.setItems(FXCollections.observableArrayList(datos));
        if (actual != null && datos.contains(actual)) {
            combo.setValue(actual);
        }
    }

    private void configurarTablaCarrito() {
        tablaCarrito.getColumns().add(columna("Producto", "nombre", 260.0));
        tablaCarrito.getColumns().add(columna("Cantidad", "cantidad", 90.0));
        tablaCarrito.getColumns().add(columna("P. Unitario", "precioUnitario", 110.0));
        tablaCarrito.getColumns().add(columna("Subtotal", "subtotal", 110.0));

        TableColumn<ItemCarrito, Void> acciones = new TableColumn<>("Acciones");
        acciones.setPrefWidth(110.0);
        acciones.setCellFactory(col -> new TableCell<ItemCarrito, Void>() {
            private final Button btnQuitar = new Button("Quitar");

            {
                btnQuitar.getStyleClass().addAll("btn", "btn-danger");
                btnQuitar.setOnAction(e -> {
                    if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                        return;
                    }
                    quitar(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnQuitar);
            }
        });
        tablaCarrito.getColumns().add(acciones);
        tablaCarrito.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private void configurarTablaVentas() {
        tablaVentas.getColumns().add(columnaVenta("N°", "idVenta", 60.0));
        tablaVentas.getColumns().add(columnaVenta("Fecha", "fechaTexto", 140.0));
        tablaVentas.getColumns().add(columnaVenta("Cliente", "clienteNombre", 240.0));
        tablaVentas.getColumns().add(columnaVenta("Entradas", "cantidadEntradas", 80.0));
        tablaVentas.getColumns().add(columnaVenta("Total (S/)", "total", 100.0));
        tablaVentas.getColumns().add(columnaVenta("Pago", "tipoPagoTexto", 90.0));
        tablaVentas.getColumns().add(columnaVenta("Usuario", "usuarioNombre", 100.0));
        tablaVentas.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<ItemCarrito, Object> columna(String titulo, String campo, double ancho) {
        TableColumn<ItemCarrito, Object> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(campo));
        columna.setPrefWidth(ancho);
        return columna;
    }

    private TableColumn<Venta, Object> columnaVenta(String titulo, String campo, double ancho) {
        TableColumn<Venta, Object> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(campo));
        columna.setPrefWidth(ancho);
        return columna;
    }

    @FXML
    public void agregarProducto() {
        Producto producto = cbProducto.getValue();
        if (producto == null) {
            mensaje("Selecciona un producto", true);
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mensaje("La cantidad debe ser un número entero", true);
            return;
        }
        if (cantidad <= 0) {
            mensaje("La cantidad debe ser mayor que cero", true);
            return;
        }
        boolean sumado = carrito.agregar(producto.getIdProducto(), producto.getNombre(), producto.getPrecio(), cantidad);
        refrescarCarrito();
        txtCantidad.setText("1");
        mensaje("", false);
        if (sumado) {
            toast("Se sumó " + cantidad + " a " + producto.getNombre() + ": ahora son " + carrito.cantidadDe(producto.getIdProducto()));
        } else {
            toast(producto.getNombre() + " agregado al carrito");
        }
    }

    private void quitar(ItemCarrito item) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Deseas quitar " + item.getNombre() + " del carrito?", ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("Quitar producto");
        confirmacion.setHeaderText(null);
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        carrito.quitar(item.getIdProducto());
        refrescarCarrito();
        toast("Se eliminó " + item.getNombre() + " del carrito");
    }

    @FXML
    public void vaciarCarrito() {
        if (carrito.estaVacio()) {
            mensaje("El carrito ya está vacío", true);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Deseas vaciar todo el carrito?", ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("Vaciar carrito");
        confirmacion.setHeaderText(null);
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        carrito.vaciar();
        refrescarCarrito();
        toast("Se vació el carrito");
    }

    @FXML
    public void registrarVenta() {
        Cliente cliente = cbCliente.getValue();
        if (cliente == null) {
            mensaje("Selecciona un cliente", true);
            return;
        }
        Funcion funcion = cbFuncion.getValue();
        if (funcion == null) {
            mensaje("Selecciona una función", true);
            return;
        }
        int entradas = leerEntradas();
        if (entradas < 0) {
            mensaje("Las entradas deben ser un número entero (0 o más)", true);
            return;
        }
        if (entradas == 0 && carrito.estaVacio()) {
            mensaje("Agrega entradas o productos a la venta", true);
            return;
        }
        TipoPago tipoPago = cbTipoPago.getValue();
        if (tipoPago == null) {
            mensaje("Selecciona el tipo de pago (Efectivo, Yape o Tarjeta)", true);
            return;
        }
        Long idUsuario = SessionManager.getInstance().getUserId();
        if (idUsuario == null) {
            mensaje("No hay un usuario con sesión iniciada", true);
            return;
        }
        try {
            Usuario usuario = usuarioService.findById(idUsuario);
            List<VentaDetalle> detalles = new ArrayList<>();
            for (ItemCarrito item : carrito.items()) {
                detalles.add(VentaDetalle.builder()
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .producto(productoService.findById(item.getIdProducto()))
                        .build());
            }
            Venta venta = Venta.builder()
                    .fechaVenta(LocalDateTime.now())
                    .cantidadEntradas(entradas)
                    .total(calcularTotal())
                    .tipoPago(tipoPago)
                    .cliente(cliente)
                    .funcion(funcion)
                    .usuario(usuario)
                    .detalles(detalles)
                    .build();
            Venta guardada = ventaService.save(venta);
            carrito.vaciar();
            cbTipoPago.setValue(null);
            txtEntradas.setText("1");
            txtCantidad.setText("1");
            refrescarCarrito();
            refrescarVentas();
            mensaje("", false);
            toast("Venta N° " + guardada.getIdVenta() + " registrada por S/ " + formato(guardada.getTotal()));
            mostrarTicket(guardada);
        } catch (RuntimeException e) {
            mensaje(e.getMessage() == null || e.getMessage().isBlank() ? "No se pudo registrar la venta" : e.getMessage(), true);
        }
    }

    @FXML
    public void verTicket() {
        Venta venta = tablaVentas.getSelectionModel().getSelectedItem();
        if (venta == null) {
            mensaje("Selecciona una venta de la tabla para ver su ticket", true);
            return;
        }
        mensaje("", false);
        mostrarTicket(venta);
    }

    private void mostrarTicket(Venta v) {
        TextArea area = new TextArea(construirTicket(v));
        area.setEditable(false);
        area.setFont(Font.font("Monospaced", 13));
        area.setPrefColumnCount(44);
        area.setPrefRowCount(Math.min(30, 18 + (v.getDetalles() == null ? 0 : v.getDetalles().size())));
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ticket de venta");
        alerta.setHeaderText("Ticket N° " + v.getIdVenta());
        alerta.getDialogPane().setContent(area);
        alerta.showAndWait();
    }

    private String construirTicket(Venta v) {
        String linea = "-".repeat(42);
        StringBuilder sb = new StringBuilder();
        sb.append("           CINEPLANET\n").append(linea).append("\n");
        sb.append("Ticket N°: ").append(v.getIdVenta()).append("\n");
        sb.append("Fecha    : ").append(v.getFechaTexto()).append("\n");
        sb.append("Cliente  : ").append(v.getClienteNombre()).append("\n");
        sb.append("Atendió  : ").append(v.getUsuarioNombre()).append("\n").append(linea).append("\n");
        Funcion f = v.getFuncion();
        if (f != null) {
            sb.append("Película : ").append(f.getPelicula() == null ? "" : f.getPelicula().getTitulo()).append("\n");
            if (f.getSala() != null) {
                sb.append("Sala     : ").append(f.getSala().getNumero()).append(" (").append(f.getSala().getTipo()).append(")\n");
            }
            sb.append("Función  : ").append(f.getFecha()).append(" ").append(f.getHoraInicio()).append("\n");
            sb.append(String.format(Locale.US, "Entradas : %d x S/ %.2f%n", v.getCantidadEntradas(),
                    f.getPrecioEntrada() == null ? 0.0 : f.getPrecioEntrada()));
        }
        if (v.getDetalles() != null && !v.getDetalles().isEmpty()) {
            sb.append(linea).append("\n");
            for (VentaDetalle d : v.getDetalles()) {
                sb.append(String.format(Locale.US, "%d x %s  S/ %.2f%n", d.getCantidad(), d.getProducto().getNombre(),
                        d.getCantidad() * d.getPrecioUnitario()));
            }
        }
        sb.append(linea).append("\n");
        sb.append(String.format(Locale.US, "TOTAL    : S/ %.2f%n", v.getTotal()));
        sb.append("Tipo de pago: ").append(v.getTipoPagoTexto()).append("\n").append(linea).append("\n");
        sb.append("     ¡Gracias por su compra!\n");
        return sb.toString();
    }

    private int leerEntradas() {
        try {
            int valor = Integer.parseInt(txtEntradas.getText().trim());
            return valor < 0 ? -1 : valor;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double subtotalEntradas() {
        Funcion funcion = cbFuncion.getValue();
        int entradas = leerEntradas();
        if (funcion == null || funcion.getPrecioEntrada() == null || entradas < 0) {
            return 0.0;
        }
        return redondear(entradas * funcion.getPrecioEntrada());
    }

    private double calcularTotal() {
        return redondear(subtotalEntradas() + carrito.total());
    }

    private void refrescarCarrito() {
        tablaCarrito.setItems(FXCollections.observableArrayList(carrito.items()));
        actualizarTotales();
    }

    private void refrescarVentas() {
        List<Venta> ventas = new ArrayList<>(ventaService.findAll());
        Collections.reverse(ventas);
        tablaVentas.setItems(FXCollections.observableArrayList(ventas));
    }

    private void actualizarTotales() {
        lblSubEntradas.setText("S/ " + formato(subtotalEntradas()));
        lblSubProductos.setText("S/ " + formato(carrito.total()));
        lblTotal.setText("S/ " + formato(calcularTotal()));
    }

    private void mensaje(String texto, boolean error) {
        lblMsg.setText(texto);
        lblMsg.setStyle(error ? "-fx-text-fill: red; -fx-font-size: 14px;" : "-fx-text-fill: green; -fx-font-size: 14px;");
    }

    private void toast(String texto) {
        Stage stage = (Stage) miContenedor.getScene().getWindow();
        Toast.showToast(stage, texto, 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private String formato(Double valor) {
        return String.format(Locale.US, "%.2f", valor == null ? 0.0 : valor);
    }
}
