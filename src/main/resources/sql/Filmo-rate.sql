CREATE TABLE "film" (
  "id" bigint PRIMARY KEY,
  "name" varchar(100),
  "description" varchar(254),
  "release_date" date,
  "duration" int,
  "mpa" bigint
);

CREATE TABLE "user" (
  "id" bigint PRIMARY KEY,
  "email" varchar(50),
  "login" varchar(50),
  "name" varchar(50),
  "birthday" date
);

CREATE TABLE "user_friends" (
  "user_id" bigint,
  "friend_id" bigint,
  PRIMARY KEY ("user_id", "friend_id")
);

CREATE TABLE "user_friendships" (
  "source_id" bigint,
  "destination_id" bigint,
  "agree" boolean,
  PRIMARY KEY ("source_id", "destination_id")
);

CREATE TABLE "film_likes" (
  "user_id" bigint,
  "film_id" bigint,
  PRIMARY KEY ("user_id", "film_id")
);

CREATE TABLE "film_genres" (
  "film_id" bigint,
  "genre_id" bigint,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE "genre" (
  "id" bigint PRIMARY KEY,
  "name" varchar(50)
);

CREATE TABLE "mpa" (
  "id" bigint PRIMARY KEY,
  "name" varchar(50)
);

ALTER TABLE "film" ADD FOREIGN KEY (mpa_ids) REFERENCES "mpa" ("id");

ALTER TABLE "user_friends" ADD FOREIGN KEY ("user_id") REFERENCES user1 ("id");

ALTER TABLE "user_friends" ADD FOREIGN KEY ("friend_id") REFERENCES user1 ("id");

ALTER TABLE "user_friendships" ADD FOREIGN KEY ("source_id") REFERENCES user1 ("id");

ALTER TABLE "user_friendships" ADD FOREIGN KEY ("destination_id") REFERENCES user1 ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("user_id") REFERENCES user1 ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");
