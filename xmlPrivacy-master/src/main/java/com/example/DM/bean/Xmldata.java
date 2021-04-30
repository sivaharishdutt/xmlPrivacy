package com.example.DM.bean;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "xmldata")
public class Xmldata implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer xml_id;

    private String xml_text;

    public Xmldata(Integer xml_id, String xml_text) {
        this.xml_id=xml_id;
        this.xml_text=xml_text;
    }

    public Xmldata() {

    }

    public String getXml_text() {
        return xml_text;
    }




}
