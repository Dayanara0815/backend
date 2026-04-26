package com.utp.adoptappbackend.pet.model;

import com.utp.adoptappbackend.common.model.enumeration.Size;
import com.utp.adoptappbackend.common.model.enumeration.Species;
import com.utp.adoptappbackend.common.model.enumeration.Status;
import com.utp.adoptappbackend.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    private String age;

    private String color;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
