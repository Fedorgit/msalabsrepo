package org.msa.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.msa.library.mapper.util.BaseEntity;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 256)
    @NotNull
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 512)
    @Column(name = "file_path", length = 512)
    private String filePath;

    @NotNull
    @Column(name = "publication_date", nullable = false)
    private Instant publicationDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "books")
    private Set<Category> categories = new LinkedHashSet<>();

}