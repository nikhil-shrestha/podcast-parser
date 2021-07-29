CREATE TABLE podcast
(
    id                           serial NOT NULL PRIMARY KEY,
    wrapper_type                 varchar(30),
    kind                         varchar(30),
    collection_id                bigint,
    artist_name                  varchar(100),
    artist_view_url              varchar(255),
    collection_name              varchar(100),
    collection_view_url          varchar(255),
    feed_url                     varchar(255),
    preview_url                  varchar(255),
    artwork_url_30               varchar(255),
    artwork_url_60               varchar(255),
    artwork_url_100              varchar(255),
    artwork_url_512              varchar(255),
    artwork_url_600              varchar(255),
    release_date                 varchar(30),
    track_count                  int,
    copyright                    varchar(50),
    country                      varchar(50),
    short_description            varchar(1000),
    long_description             text,
    description                  text,
    current_version_release_date varchar(30),
    episode_count                int
);

create table episode
(
    id              serial NOT NULL PRIMARY KEY,
    podcast         bigint,
    title           varchar(50),
    description     text,
    guid            varchar(255),
    hosted_url      varchar(255),
    episode_number  varchar(50),
    duration_string varchar(50),
    duration        bigint,
    link            varchar(255),
    type            varchar(30),
    constraint fk_episode_podcast_id foreign key (podcast) references podcast(id)
);

create table genre
(
    id           serial NOT NULL PRIMARY KEY,
    name         varchar(30),
    genre_id_old int
);

create table podcast_genre
(
    genre bigint,
    podcast bigint,
    primary key (genre, podcast)
)