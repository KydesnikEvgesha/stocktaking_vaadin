package ru.kravchenkoei.stocktaking.views.dashboard;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.kravchenkoei.stocktaking.views.MainLayout;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
@Tag("dashboard-view")
@JsModule("./views/dashboard/dashboard-view.ts")
public class DashboardView extends LitTemplate implements HasStyle {

    public DashboardView() {
        addClassNames("flex", "flex-col", "h-full");
    }
}
