package ru.java.mentor.oldranger.club.model.user.media;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "photoAlbum")
@DynamicUpdate
public class PhotoAlbum {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @JoinColumn(name = "skin")
    @OneToOne
    private Photo skin;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "album_photos", joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id"))
    private List<Photo> photos = new ArrayList<>();

    public PhotoAlbum(String title) {
        this.title = title;
    }
}
