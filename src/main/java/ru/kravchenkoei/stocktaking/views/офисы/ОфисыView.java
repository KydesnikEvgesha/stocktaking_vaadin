package ru.kravchenkoei.stocktaking.views.офисы;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.kravchenkoei.stocktaking.data.entity.Location;
import ru.kravchenkoei.stocktaking.data.service.LocationService;
import ru.kravchenkoei.stocktaking.views.MainLayout;

@PageTitle("Офисы")
@Route(value = "location/:locationID?/:action?(edit)", layout = MainLayout.class)
@Tag("офисы-view")
@JsModule("./views/офисы/офисы-view.ts")
public class ОфисыView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String LOCATION_ID = "locationID";
    private final String LOCATION_EDIT_ROUTE_TEMPLATE = "location/%d/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Location> grid;

    @Id
    private TextField name;
    @Id
    private TextField address;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Location> binder;

    private Location location;

    private LocationService locationService;

    public ОфисыView(@Autowired LocationService locationService) {
        this.locationService = locationService;
        addClassNames("офисы-view", "flex", "flex-col", "h-full");
        grid.addColumn(Location::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Location::getAddress).setHeader("Address").setAutoWidth(true);
        grid.setItems(query -> locationService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(LOCATION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ОфисыView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Location.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.location == null) {
                    this.location = new Location();
                }
                binder.writeBean(this.location);

                locationService.update(this.location);
                clearForm();
                refreshGrid();
                Notification.show("Location details stored.");
                UI.getCurrent().navigate(ОфисыView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the location details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> locationId = event.getRouteParameters().getInteger(LOCATION_ID);
        if (locationId.isPresent()) {
            Optional<Location> locationFromBackend = locationService.get(locationId.get());
            if (locationFromBackend.isPresent()) {
                populateForm(locationFromBackend.get());
            } else {
                Notification.show(String.format("The requested location was not found, ID = %d", locationId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ОфисыView.class);
            }
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Location value) {
        this.location = value;
        binder.readBean(this.location);

    }
}
