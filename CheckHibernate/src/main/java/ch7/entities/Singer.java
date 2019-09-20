package ch7.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "singer") //этот класс сущности преобразуется в таблицу singer
//fetch ниже пишется для того, чтобы библиотеке
//Hiberпate предписывается немедленно произвести выборку связи
@NamedQueries({
        @NamedQuery(name="Singer.findById",
                query="select distinct s from Singer s " +
                        "left join fetch s.albums a " +
                        "left join fetch s.instruments i " +
                        "where s.id = :id"),
        @NamedQuery(name="Singer.findAllWithAlbum",
                query="select distinct s from Singer s " +
                        "left join fetch s.albums a " +
                        "left join fetch s.instruments i")
})
public class Singer implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private int version;
    private Set<Album> albums = new HashSet<>();
    private Set<Instrument> instruments = new HashSet<>();

    public void setId(Long id) {
        this.id = id;
    }

    @Id   //это означает, что он является первичным ключом
    @GeneratedValue(strategy = IDENTITY) //id сгенерирован СУРБД
    @Column(name = "ID")
    public Long getId() {
        return this.id;
    }

    //Всякий раз, когда библиотека Hibernate обновляет заnись, она сравнивает версию экземnляра сущности с версией
    // заnиси в базе данных. Если версии совnадают, значит, данные раньше не обновлялись, и nоэтому HiЬernate
    // обновит данные и увеличит значение в столбце версии.
    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return this.firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return this.lastName;
    }

    @Temporal(TemporalType.DATE) //для преобразования java.util.Data к java.sql.Date
    @Column(name = "BIRTH_DATE")
    public Date getBirthDate() {
        return birthDate;
    }

    @OneToMany(mappedBy = "singer", cascade=CascadeType.ALL,
            orphanRemoval=true)
    public Set<Album> getAlbums() {
        return albums;
    }


    @ManyToMany
    @JoinTable(name = "singer_instrument",
            joinColumns = @JoinColumn(name = "SINGER_ID"),
            inverseJoinColumns = @JoinColumn(name = "INSTRUMENT_ID"))
    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean addAlbum(Album album) {
        album.setSinger(this);
        return getAlbums().add(album);
    }

    public void removeAlbum(Album album) {
        getAlbums().remove(album);
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public boolean addInstrument(Instrument instrument) {
        return getInstruments().add(instrument);
    }

    public String toString() {
        return "Singer - Id: " + id + ", First name: " + firstName
                + ", Last name: " + lastName + ", Birthday: " + birthDate;
    }
}
