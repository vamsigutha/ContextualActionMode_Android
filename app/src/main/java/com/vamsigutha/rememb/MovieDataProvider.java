package com.vamsigutha.rememb;

public class MovieDataProvider{

    private String title;
    private String optionButton;
    private String circle;

    public MovieDataProvider(String circle, String title,String optionButton){

        this.setTitle(title);
        this.setOptionButton(optionButton);
        this.setCircle(circle);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }





    public String getOptionButton(){
        return optionButton;
    }

    public void setOptionButton(String optionButton){
        this.optionButton = optionButton;
    }

    public String getCircle(){
        return circle;
    }

    public void setCircle(String circle){
        this.circle=circle;
    }
}
