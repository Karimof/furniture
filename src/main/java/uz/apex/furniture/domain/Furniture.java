package uz.apex.furniture.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.apex.furniture.domain.enumeration.Installation;

/**
 * A Furniture.
 */
@Entity
@Table(name = "furniture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Furniture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "photo", nullable = false)
    private String photo;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @NotNull
    @Size(max = 64)
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "installation", nullable = false)
    private Installation installation;

    @ManyToOne
    private Types type;

    @ManyToOne
    private Brand brand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Furniture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return this.photo;
    }

    public Furniture photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImage() {
        return this.image;
    }

    public Furniture image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public Furniture name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Installation getInstallation() {
        return this.installation;
    }

    public Furniture installation(Installation installation) {
        this.setInstallation(installation);
        return this;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    public Types getType() {
        return this.type;
    }

    public void setType(Types types) {
        this.type = types;
    }

    public Furniture type(Types types) {
        this.setType(types);
        return this;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Furniture brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Furniture)) {
            return false;
        }
        return id != null && id.equals(((Furniture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Furniture{" +
            "id=" + getId() +
            ", photo='" + getPhoto() + "'" +
            ", image='" + getImage() + "'" +
            ", name='" + getName() + "'" +
            ", installation='" + getInstallation() + "'" +
            "}";
    }
}
