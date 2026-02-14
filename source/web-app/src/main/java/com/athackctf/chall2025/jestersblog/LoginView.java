package com.athackctf.chall2025.jestersblog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final UsersService usersService;

    private final LoginOverlay loginOverlay;

    public LoginView(UsersService usersService) {
        loginOverlay = new LoginOverlay();
        loginOverlay.setOpened(true);
        loginOverlay.setTitle("Welcome Back, Zorvax");
        loginOverlay.setDescription("No humans allowed.");
        loginOverlay.setForgotPasswordButtonVisible(false);

        this.usersService = usersService;

        Button closeButton = new Button("Back", event -> {
            loginOverlay.close();
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.getStyle().set("position", "absolute")
                .set("top", "10px")
                .set("left", "10px")
                .set("z-index", "9999");

        loginOverlay.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();
            boolean authenticated = authenticate(username, password);

            if (authenticated) {
                loginOverlay.close();
                showSuccessPopup();
            } else {
                loginOverlay.setError(true);
            }
        });

        add(closeButton, loginOverlay);
    }

    private boolean authenticate(String username, String password) {
        return usersService.validateUser(username, password);
    }

    private void showSuccessPopup() {
        Dialog dialog = new Dialog();
        dialog.add(new H2("Authentication Successful!"),
                new Paragraph("Here's your Flag: ATHACKCTF{Z0RV4X_THE_D3S7R0Y3R}"));
        dialog.setWidth("400px");
        dialog.setHeight("200px");

        dialog.open();
    }
}
