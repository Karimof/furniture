package uz.apex.furniture.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import uz.apex.furniture.domain.enumeration.Installation;

/**
 * A DTO for the {@link uz.apex.furniture.domain.Furniture} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FurnitureDTO implements Serializable {

    private Long id;

    @NotNull
    private String photo;

    @NotNull
    private String image;

    @NotNull
    @Size(max = 64)
    private String name;

    @NotNull
    private Installation installation;

    private TypesDTO type;

    private BrandDTO brand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    public TypesDTO getType() {
        return type;
    }

    public void setType(TypesDTO type) {
        this.type = type;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FurnitureDTO)) {
            return false;
        }

        FurnitureDTO furnitureDTO = (FurnitureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, furnitureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FurnitureDTO{" +
            "id=" + getId() +
            ", photo='" + getPhoto() + "'" +
            ", image='" + getImage() + "'" +
            ", name='" + getName() + "'" +
            ", installation='" + getInstallation() + "'" +
            ", type=" + getType() +
            ", brand=" + getBrand() +
            "}";
    }
}
