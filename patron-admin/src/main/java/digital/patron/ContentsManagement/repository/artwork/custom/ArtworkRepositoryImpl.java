package digital.patron.ContentsManagement.repository.artwork.custom;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.ContentsManagement.domain.artist.QSurviveArtist;
import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.repository.artwork.custom.ArtworkRepositoryCustom;

import java.util.List;

import static digital.patron.ContentsManagement.domain.artist.QDeathArtist.deathArtist;
import static digital.patron.ContentsManagement.domain.artist.QSurviveArtist.surviveArtist;
import static digital.patron.ContentsManagement.domain.artwork.QArtwork.artwork;
import static digital.patron.ContentsManagement.domain.artwork.QArtworkArtworkTag.artworkArtworkTag;
import static digital.patron.ContentsManagement.domain.artwork.QArtworkTag.artworkTag;
import static digital.patron.ContentsManagement.domain.artwork.QContents4k.contents4k;
import static digital.patron.ContentsManagement.domain.artwork.QContentsHd.contentsHd;
import static digital.patron.ContentsManagement.domain.artwork.QContentsThumbnail.contentsThumbnail;
import static digital.patron.ContentsManagement.domain.artwork.QSound.sound;
import static digital.patron.PatronMembers.domain.QBusinessMember.businessMember;


public class ArtworkRepositoryImpl implements ArtworkRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ArtworkRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Artwork> findNoApprovalArtworksByKeyword(String keyword) {
        StringTemplate keywordWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", keyword);
        StringTemplate deathArtistKorWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", deathArtist.korName);
        StringTemplate deathArtistEngWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", deathArtist.korName);
        StringTemplate surviveArtistKorWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", surviveArtist.korName);
        StringTemplate surviveArtistEngWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", surviveArtist.engName);
        StringTemplate ArtworkNameWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", artwork.artworkName);
        return queryFactory
                .select(artwork)
                .from(artwork)
                .where((ArtworkNameWithoutSpace.like(keywordWithoutSpace)
                        .or(deathArtistKorWithoutSpace.like(keywordWithoutSpace))
                        .or(deathArtistEngWithoutSpace.like(keywordWithoutSpace))
                        .or(surviveArtistKorWithoutSpace.like(keywordWithoutSpace))
                        .or(surviveArtistEngWithoutSpace.like(keywordWithoutSpace))
                        //.or(artwork.email.eq(keyword))
                        ).and(artwork.approve.eq(false)))
                .fetch();
    }
    @Override
    public List<Artwork> findApprovedArtworksByKeyword(String keyword) {
        StringTemplate keywordWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", keyword);
        StringTemplate deathArtistKorWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", deathArtist.korName);
        StringTemplate deathArtistEngWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", deathArtist.korName);
        StringTemplate surviveArtistKorWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", surviveArtist.korName);
        StringTemplate surviveArtistEngWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", surviveArtist.engName);
        StringTemplate ArtworkNameWithoutSpace = Expressions.stringTemplate("replace({0},' ','')", artwork.artworkName);
        return queryFactory
                .select(artwork)
                .from(artwork)
                .where((ArtworkNameWithoutSpace.like(keywordWithoutSpace)
                        .or(deathArtistKorWithoutSpace.like(keywordWithoutSpace))
                        .or(deathArtistEngWithoutSpace.like(keywordWithoutSpace))
                        .or(surviveArtistKorWithoutSpace.like(keywordWithoutSpace))
                        .or(surviveArtistEngWithoutSpace.like(keywordWithoutSpace))
                        //.or(artwork.email.eq(keyword))
                ).and(artwork.approve.eq(true)))
                .fetch();
    }

    @Override
    public Artwork findArtworkById(Long artworkId) {
        return queryFactory
                .select(artwork)
                .from(artwork)
                .join(artwork.contentsThumbnail, contentsThumbnail).fetchJoin()
                .leftJoin(artwork.deathArtist, deathArtist).fetchJoin()
                .leftJoin(artwork.surviveArtist, surviveArtist).fetchJoin()
                .leftJoin(artwork.contents4k, contents4k).fetchJoin()
                .leftJoin(artwork.contentsHd, contentsHd).fetchJoin()
                .join(artwork.sound, sound).fetchJoin()
                .join(artwork.artworkArtworkTags, artworkArtworkTag).fetchJoin()
                .join(artworkArtworkTag.artworkTag, artworkTag).fetchJoin()
                .where(artwork.id.eq(artworkId))
                .fetchFirst();
    }


}
