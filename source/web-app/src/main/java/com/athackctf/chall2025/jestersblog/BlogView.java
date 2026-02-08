package com.athackctf.chall2025.jestersblog;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.Map;
import java.util.Objects;

@PageTitle("Blog Post")
@Route("blog")
@AnonymousAllowed
public class BlogView extends VerticalLayout implements HasUrlParameter<String> {

    private final BlogPostsService blogPostsService;

    public BlogView(BlogPostsService blogPostsService) {
        this.blogPostsService = blogPostsService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    Button closeButton = new Button("Back", event -> {
        getUI().ifPresent(ui -> ui.navigate(""));
    });

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String postId) {
        var JestersBlogLogo = new Image("img/JestersBlog.png", "Jester");

        JestersBlogLogo.setHeight(150, Unit.PIXELS);
        var logoLayout = new HorizontalLayout(JestersBlogLogo);
        logoLayout.setAlignItems(Alignment.CENTER);
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setWidthFull();

        HorizontalLayout closeLayout = new HorizontalLayout(closeButton);
        closeLayout.setWidthFull();
        closeLayout.setJustifyContentMode(JustifyContentMode.START);

        add(closeLayout);
        add(logoLayout);
        if (!Objects.equals(postId, "")) {
            Map<String, Object> post = blogPostsService.getBlogPostById(postId);
            if (post != null) {
                String title = post.get("title").toString();
                String body = "<div>" + post.get("body").toString()+ "</div>";
                add(new H1(title), new Html(body));
            } else {
                add(new H1("Post Not Found"), new Paragraph("The requested blog post does not exist."));
            }
        }
    }
}
