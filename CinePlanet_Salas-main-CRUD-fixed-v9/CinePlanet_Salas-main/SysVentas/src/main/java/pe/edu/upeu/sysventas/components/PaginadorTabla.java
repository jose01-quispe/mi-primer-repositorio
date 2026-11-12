package pe.edu.upeu.sysventas.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;

/**
 * Paginación reutilizable para cualquier TableView (investigación adicional).
 *
 * Cómo funciona:
 *  1. Recibe la lista completa (ya filtrada por el buscador).
 *  2. La envuelve en un SortedList para que ordenar por columna afecte a TODAS las páginas,
 *     no solo a la página visible.
 *  3. Muestra en la tabla únicamente la "rebanada" (subList) de la página actual.
 *  4. Dibuja los controles: « ‹ Página X de Y › »  +  filas por página  +  "Mostrando a–b de N".
 */
public class PaginadorTabla<T> {
    private final TableView<T> tableView;
    private final ObservableList<T> pagina = FXCollections.observableArrayList();
    private final ListChangeListener<T> alCambiarOrigen = cambio -> refrescar();
    private SortedList<T> ordenada;

    private int paginaActual = 0;
    private int filasPorPagina = 10;

    private final Button btnPrimera = new Button("«");
    private final Button btnAnterior = new Button("‹");
    private final Button btnSiguiente = new Button("›");
    private final Button btnUltima = new Button("»");
    private final Label lblPagina = new Label();
    private final Label lblResumen = new Label();
    private final ComboBox<Integer> cbFilas = new ComboBox<>();
    private final HBox contenedor = new HBox(8);

    public PaginadorTabla(TableView<T> tableView) {
        this.tableView = tableView;
        tableView.setItems(pagina);
        // El orden lo hace el SortedList (sobre todas las filas); la tabla no reordena solo la página.
        tableView.setSortPolicy(tv -> true);

        cbFilas.getItems().addAll(5, 10, 20, 50);
        cbFilas.setValue(filasPorPagina);
        cbFilas.valueProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                filasPorPagina = nuevo;
                paginaActual = 0;
                refrescar();
            }
        });

        btnPrimera.setOnAction(e -> irA(0));
        btnAnterior.setOnAction(e -> irA(paginaActual - 1));
        btnSiguiente.setOnAction(e -> irA(paginaActual + 1));
        btnUltima.setOnAction(e -> irA(totalPaginas() - 1));

        for (Button b : new Button[]{btnPrimera, btnAnterior, btnSiguiente, btnUltima}) {
            b.getStyleClass().add("btn-pag");
        }
        lblPagina.getStyleClass().add("paginador-pagina");
        lblResumen.getStyleClass().add("paginador-resumen");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        contenedor.setAlignment(Pos.CENTER_LEFT);
        contenedor.getStyleClass().add("paginador");
        contenedor.getChildren().addAll(lblResumen, espacio, new Label("Filas:"), cbFilas,
                btnPrimera, btnAnterior, lblPagina, btnSiguiente, btnUltima);
        refrescar();
    }

    /** Nodo con los controles de paginación para agregar a la vista. */
    public HBox getNode() {
        return contenedor;
    }

    /** Define la lista completa (normalmente un FilteredList). Mantiene la página actual si sigue existiendo. */
    public void setItems(ObservableList<T> fuente) {
        if (ordenada != null) {
            ordenada.removeListener(alCambiarOrigen);
            ordenada.comparatorProperty().unbind();
        }
        ordenada = new SortedList<>(fuente);
        ordenada.comparatorProperty().bind(tableView.comparatorProperty());
        ordenada.addListener(alCambiarOrigen);
        refrescar();
    }

    /** Útil al buscar: vuelve a la página 1. */
    public void primeraPagina() {
        irA(0);
    }

    private void irA(int pagina) {
        paginaActual = Math.max(0, Math.min(pagina, totalPaginas() - 1));
        refrescar();
    }

    private int totalRegistros() {
        return ordenada == null ? 0 : ordenada.size();
    }

    private int totalPaginas() {
        return Math.max(1, (int) Math.ceil(totalRegistros() / (double) filasPorPagina));
    }

    private void refrescar() {
        int total = totalRegistros();
        int paginas = totalPaginas();
        if (paginaActual >= paginas) paginaActual = paginas - 1;
        if (paginaActual < 0) paginaActual = 0;

        int desde = paginaActual * filasPorPagina;
        int hasta = Math.min(desde + filasPorPagina, total);
        pagina.setAll(total == 0 ? new ArrayList<>() : new ArrayList<>(ordenada.subList(desde, hasta)));

        lblPagina.setText("Página " + (paginaActual + 1) + " de " + paginas);
        lblResumen.setText(total == 0 ? "Sin registros" : "Mostrando " + (desde + 1) + "–" + hasta + " de " + total);
        btnPrimera.setDisable(paginaActual == 0);
        btnAnterior.setDisable(paginaActual == 0);
        btnSiguiente.setDisable(paginaActual >= paginas - 1);
        btnUltima.setDisable(paginaActual >= paginas - 1);
    }
}
