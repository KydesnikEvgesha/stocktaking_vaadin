package ru.kravchenkoei.stocktaking.views.контрагенты;

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
import ru.kravchenkoei.stocktaking.data.entity.Company;
import ru.kravchenkoei.stocktaking.data.service.CompanyService;
import ru.kravchenkoei.stocktaking.views.MainLayout;

@PageTitle("Контрагенты")
@Route(value = "company/:companyID?/:action?(edit)", layout = MainLayout.class)
@Tag("контрагенты-view")
@JsModule("./views/контрагенты/контрагенты-view.ts")
public class КонтрагентыView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String COMPANY_ID = "companyID";
    private final String COMPANY_EDIT_ROUTE_TEMPLATE = "company/%d/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Company> grid;

    @Id
    private TextField name;
    @Id
    private TextField address;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Company> binder;

    private Company company;

    private CompanyService companyService;

    public КонтрагентыView(@Autowired CompanyService companyService) {
        this.companyService = companyService;
        addClassNames("контрагенты-view", "flex", "flex-col", "h-full");
        grid.addColumn(Company::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Company::getAddress).setHeader("Address").setAutoWidth(true);
        grid.setItems(query -> companyService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(COMPANY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(КонтрагентыView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Company.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.company == null) {
                    this.company = new Company();
                }
                binder.writeBean(this.company);

                companyService.update(this.company);
                clearForm();
                refreshGrid();
                Notification.show("Company details stored.");
                UI.getCurrent().navigate(КонтрагентыView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the company details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> companyId = event.getRouteParameters().getInteger(COMPANY_ID);
        if (companyId.isPresent()) {
            Optional<Company> companyFromBackend = companyService.get(companyId.get());
            if (companyFromBackend.isPresent()) {
                populateForm(companyFromBackend.get());
            } else {
                Notification.show(String.format("The requested company was not found, ID = %d", companyId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(КонтрагентыView.class);
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

    private void populateForm(Company value) {
        this.company = value;
        binder.readBean(this.company);

    }
}
