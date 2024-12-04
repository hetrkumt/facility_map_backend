package com.kitHub.Facilities_info.domain.Report;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kitHub.Facilities_info.domain.User;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "blocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    @JsonManagedReference
    private User blockedUser;

    @OneToMany(mappedBy = "block")
    @JsonManagedReference
    private Set<Report> reports;

    @Column(nullable = false)
    private LocalDateTime blockDate;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDateTime unblockDate;

    @Column
    private String adminComments;

    @Builder
    public Block(User blockedUser, Set<Report> reports, LocalDateTime blockDate, String status, LocalDateTime unblockDate, String adminComments) {
        this.blockedUser = blockedUser;
        this.reports = reports;
        this.blockDate = blockDate;
        this.status = status;
        this.unblockDate = unblockDate;
        this.adminComments = adminComments;
    }
}
