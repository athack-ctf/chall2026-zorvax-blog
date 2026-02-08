package com.athackctf.chall2025.jestersblog;

import com.athackctf.chall2025.jestersblog.BlogPostsService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.server.StreamResource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Jester's Blog")
@Route("")
public class MainView extends VerticalLayout {
    private final BlogPostsService blogPostService;
    private final Grid<BlogPosts> blogGrid;

    @Autowired
    public MainView(BlogPostsService blogPostService) {
        var searchField = new TextField();
        var searchButton = new Button("Search!");
        searchField.setPlaceholder("Enter ID");
        searchField.setWidth("500px");

        var JestersBlogLogo = new Image("img/JestersBlog.png", "Jester");
        var strJesters = new H1("Jester's Blog");

        this.blogPostService = blogPostService;

        blogGrid = new Grid<>(BlogPosts.class, false);
        blogGrid.addColumn(BlogPosts::getId).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        blogGrid.addComponentColumn(blogPost -> {
            Anchor link = new Anchor("/blog/" + blogPost.getId(), blogPost.getTitle());
            link.getElement().setAttribute("router-link", true);
            return link;
        }).setHeader("Title").setAutoWidth(true).setFlexGrow(0);
        blogGrid.addColumn(post -> {
            return post.getBody().replaceAll("<[^>]*>", "");
        }).setHeader("Body").setTextAlign(ColumnTextAlign.CENTER);

        Button loginButton = new Button("Go to Login", event ->
                getUI().ifPresent(ui -> ui.navigate("login"))
        );
        loginButton.getStyle().set("margin-left", "auto");


        searchButton.addClickListener(event -> {
            String searchQuery = searchField.getValue();
            List<BlogPosts> filteredPosts = searchQuery.isEmpty()
                    ? blogPostService.getAllBlogPosts()
                    : blogPostService.searchById(searchQuery);
            blogGrid.setItems(filteredPosts);
        });

        blogGrid.setHeightFull();
        //blogGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        blogGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        blogGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

        HorizontalLayout topBar = new HorizontalLayout(loginButton);
        topBar.setWidthFull();
        topBar.setPadding(true);
        topBar.getStyle().set("justify-content", "flex-end");

        JestersBlogLogo.setHeight(150, Unit.PIXELS);
        var logoLayout = new HorizontalLayout(JestersBlogLogo);
        logoLayout.setAlignItems(Alignment.CENTER);
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setWidthFull();

        var titleLayout = new HorizontalLayout(strJesters);
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setWidthFull();

        var searchLayout = new HorizontalLayout(searchField, searchButton);
        searchLayout.setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);


        blogGrid.setItems(blogPostService.getAllBlogPosts());
        setSizeFull();

        add(topBar, logoLayout, titleLayout, searchLayout, blogGrid);
        setFlexGrow(1, blogGrid);
    }
}