package com.plac.domain.destination.entity;

import com.plac.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchWordsMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_words_id")
    private SearchWords searchWords;

    @Builder
    public UserSearchWordsMapping(User user, SearchWords searchWords) {
        this.user = user;
        this.searchWords = searchWords;
    }
}
