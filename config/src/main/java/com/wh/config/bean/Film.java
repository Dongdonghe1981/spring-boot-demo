package com.wh.config.bean;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将配置文件中配置的每个属性的值，映射到这个组件中
 * @ConfigurationProperties：将类与配置文件中的配置进行绑定
 * @Component：只有这个组件是容器中的组件，才能提供@ConfigurationProperties功能
 */
@PropertySource(value ={"classpath:film.yml"})
//@ConfigurationProperties(prefix="film")
@Component
public class Film {

    private String title;
    private String year;
    private Integer length;
    private List<Actor> actors;

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", length=" + length +
                ", actors=" + actors +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

}
