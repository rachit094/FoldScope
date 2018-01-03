package com.example.blackpearl.foldscope.Model;

public class CategoryData {

    public String Categories_id;
    public String Categories_title;
    public String slug;

    public String getSlug() {

        return slug;
    }

    public void setSlug(String slug) {

        this.slug = slug;
    }

    public String getCategories_id() {

        return Categories_id;
    }

    public void setCategories_id(String categories_id) {

        Categories_id = categories_id;
    }

    public String getCategories_title() {

        return Categories_title;
    }

    public void setCategories_title(String categories_title) {

        Categories_title = categories_title;
    }
}