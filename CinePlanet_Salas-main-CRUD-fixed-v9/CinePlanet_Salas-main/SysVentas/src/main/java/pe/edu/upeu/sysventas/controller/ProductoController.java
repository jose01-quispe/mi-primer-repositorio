package pe.edu.upeu.sysventas.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.PaginadorTabla;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.components.ToltipCustom;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.service.IProductoService;

import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService ps;

    @FXML
    TextField txtNombreProducto;
    @FXML
    TextField txtPrecioProducto;
    @FXML
    TextField txtFiltroDato;
    @FXML
    Button btnGuardar;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    @FXML
    private HBox boxPaginacion;
    private PaginadorTabla<Producto> paginador;

    ObservableList<Producto> listarProducto;

    Producto formulario;

    Long idProductoCE = 0L;

    Stage stage;

    private Validator validator;

    private final ToltipCustom ttc = new ToltipCustom();


    @FXML
    public void initialize() {

        System.out.println("ProductoController iniciado");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Prod.", new ColumnInfo("idProducto", 80.0)
        );

        columns.put(
                "Nombre", new ColumnInfo("nombre", 250.0)
        );

        columns.put(
                "Precio (S/)", new ColumnInfo("precio", 100.0)
        );

        Consumer<Producto> updateAction = producto -> {
            editForm(producto);
            idProductoCE = producto.getIdProducto();
        };
        Consumer<Producto> deleteAction = this::eliminar;

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        paginador = new PaginadorTabla<>(tableView);
        boxPaginacion.getChildren().add(paginador.getNode());
        listar();
        txtFiltroDato.textProperty().addListener((obs, oldValue, newValue) -> {
            listar();
            paginador.primeraPagina();
        });
    }

    @FXML
    public void buscar() {
        listar();
        paginador.primeraPagina();
    }

    @FXML
    public void editarSeleccionado() {
        Producto seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Selecciona un producto", "Debes seleccionar un producto para editarlo.");
            return;
        }
        editForm(seleccionado);
    }

    @FXML
    public void eliminarSeleccionado() {
        Producto seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Selecciona un producto", "Debes seleccionar un producto para eliminarlo.");
            return;
        }
        eliminar(seleccionado);
    }

    private void eliminar(Producto producto) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar el producto " + producto.getNombre() + "?", ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("Eliminar producto");
        confirmacion.setHeaderText(null);
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            ps.delete(producto.getIdProducto());
            if (producto.getIdProducto().equals(idProductoCE)) {
                clearForm();
            }
            Stage stage = (Stage) miContenedor.getScene().getWindow();
            double w = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
            listar();
        } catch (RuntimeException e) {
            alerta("No se pudo eliminar", mensajeError(e));
        }
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String mensajeError(RuntimeException e) {
        return e.getMessage() == null || e.getMessage().isBlank() ? "Ocurrió un error al procesar la operación." : e.getMessage();
    }


    public void listar() {

        try {
            String filtro = txtFiltroDato == null || txtFiltroDato.getText() == null ? "" : txtFiltroDato.getText().trim().toLowerCase();
            listarProducto = FXCollections.observableArrayList(ps.findAll().stream()
                    .filter(p -> filtro.isEmpty() || (p.getNombre() != null && p.getNombre().toLowerCase().contains(filtro)))
                    .toList());
            paginador.setItems(listarProducto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setStage(Stage stage) {

        this.stage = stage;

        System.out.println(
                "Llego " + stage.getTitle());
    }

    @FXML
    public void validarFormulario() {

        formulario = new Producto();

        formulario.setNombre(
                txtNombreProducto.getText() == null ? "" : txtNombreProducto.getText().trim()
        );

        String textoPrecio = txtPrecioProducto.getText() == null ? "" : txtPrecioProducto.getText().trim().replace(',', '.');
        if (!textoPrecio.isEmpty()) {
            try {
                formulario.setPrecio(Double.valueOf(textoPrecio));
            } catch (NumberFormatException e) {
                limpiarError();
                ttc.marcarError(txtPrecioProducto, "El precio debe ser un número");
                lbnMsg.setText("El precio debe ser un número");
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                Platform.runLater(txtPrecioProducto::requestFocus);
                return;
            }
        }

        Set<ConstraintViolation<Producto>> violaciones = validator.validate(formulario);

        List<ConstraintViolation<Producto>> violacionesOrdenadas = violaciones.stream().sorted(Comparator.comparing(v -> v.getPropertyPath().toString())).toList();
        if (violacionesOrdenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violacionesOrdenadas);}}
    private void mostrarErroresValidacion(
            List<ConstraintViolation<Producto>> violaciones) {

        limpiarError();
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("nombre", txtNombreProducto);
        campos.put("precio", txtPrecioProducto);
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerCtrl = {null};

        for (String campo : campos.keySet()) {
            violaciones.stream().filter(v -> v.getPropertyPath().toString().equals(campo)).findFirst().ifPresent(v -> {erroresOrdenados.put(campo, v.getMessage());
                        Control c = campos.get(campo);
                        if (c != null) {
                            ttc.marcarError(c, v.getMessage().trim());
                        }
                        if (primerCtrl[0] == null) {

                            primerCtrl[0] = c;
                        }
                    });
        }


        if (!erroresOrdenados.isEmpty()) {

            lbnMsg.setText(
                    erroresOrdenados
                            .entrySet()
                            .iterator()
                            .next()
                            .getValue()
            );

            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");

            if (primerCtrl[0] != null) {
                Platform.runLater(primerCtrl[0]::requestFocus);
            }
        }
    }


    private void procesarFormulario() {
        lbnMsg.setText("Formulario válido");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        Stage stage = (Stage) miContenedor.getScene().getWindow();
        limpiarError();
        double w = stage.getWidth() / 1.5;
        double h = stage.getHeight() / 2;
        try {
            if (idProductoCE > 0L) {
                formulario.setIdProducto(idProductoCE);
                ps.update(idProductoCE, formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, w, h);
            } else {
                ps.save(formulario);
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, w, h);
            }
        } catch (RuntimeException e) {
            lbnMsg.setText(mensajeError(e));
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            return;
        }
        clearForm();
        listar();
    }
    public void editForm(Producto producto) {
        txtNombreProducto.setText(producto.getNombre());
        txtPrecioProducto.setText(producto.getPrecio() == null ? "" : String.valueOf(producto.getPrecio()));
        idProductoCE = producto.getIdProducto();
        btnGuardar.setText("Actualizar");
        limpiarError();
    }
    public void limpiarError() {
        ttc.limpiarCampo(txtNombreProducto);
        ttc.limpiarCampo(txtPrecioProducto);
        txtNombreProducto.getStyleClass().remove("text-field-error");
    }

    @FXML
    public void clearForm() {
        txtNombreProducto.clear();
        txtPrecioProducto.clear();
        idProductoCE = 0L;
        btnGuardar.setText("Guardar");
        lbnMsg.setText("");
        limpiarError();
    }
}
