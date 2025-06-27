package com.stevenk.wholefoods.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String filetype;

    @Lob
    @JsonIgnore
    private Blob image;
    private String url;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ProductID")
    private Product product;

}
