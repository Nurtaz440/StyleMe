-- ─────────────────────────────────────────────────────────────────────────────
--  StyleMe Database Schema
--  This runs automatically on first docker-compose up via the init volume mount.
-- ─────────────────────────────────────────────────────────────────────────────

CREATE DATABASE IF NOT EXISTS styleme;
USE styleme;

-- ── Users ─────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_name     VARCHAR(64)  NOT NULL UNIQUE,
    first_name    VARCHAR(64)  NOT NULL,
    last_name     VARCHAR(64)  NOT NULL,
    email         VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    date_created  DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_updated  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── Face Shapes ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS face_shapes (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(64) NOT NULL UNIQUE,
    label VARCHAR(64)
);

INSERT IGNORE INTO face_shapes (name, label) VALUES
  ('oval',    'Oval'),
  ('round',   'Round'),
  ('square',  'Square'),
  ('heart',   'Heart'),
  ('oblong',  'Oblong / Rectangle'),
  ('diamond', 'Diamond');

-- ── Hair Colours ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS hair_colours (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(64)  NOT NULL UNIQUE,
    html_code VARCHAR(16),
    r         TINYINT UNSIGNED,
    g         TINYINT UNSIGNED,
    b         TINYINT UNSIGNED
);

INSERT IGNORE INTO hair_colours (name, html_code, r, g, b) VALUES
  ('jet_black',       '#0A0A0A', 10,  10,  10),
  ('dark_brown',      '#3B1F0E', 59,  31,  14),
  ('medium_brown',    '#6B3A2A', 107, 58,  42),
  ('light_brown',     '#A0522D', 160, 82,  45),
  ('dirty_blonde',    '#C8A96E', 200, 169, 110),
  ('golden_blonde',   '#F0C040', 240, 192, 64),
  ('platinum_blonde', '#F5E6C8', 245, 230, 200),
  ('strawberry',      '#E8846A', 232, 132, 106),
  ('auburn',          '#922B21', 146, 43,  33),
  ('copper_red',      '#C0392B', 192, 57,  43),
  ('bright_red',      '#E74C3C', 231, 76,  60),
  ('sunny_yellow',    '#F9CA24', 249, 202, 36),
  ('rose_gold',       '#E8B4B8', 232, 180, 184),
  ('pastel_pink',     '#FFB6C1', 255, 182, 193),
  ('electric_blue',   '#1A73E8', 26,  115, 232),
  ('teal',            '#009688', 0,   150, 136),
  ('violet_purple',   '#8E44AD', 142, 68,  173),
  ('silver_grey',     '#95A5A6', 149, 165, 166);

-- ── Hair Lengths ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS hair_lengths (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(64) NOT NULL UNIQUE,
    label VARCHAR(64)
);

INSERT IGNORE INTO hair_lengths (name, label) VALUES
  ('short',  'Short'),
  ('medium', 'Medium'),
  ('long',   'Long');

-- ── Hair Styles ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS hair_styles (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(64) NOT NULL UNIQUE,
    label VARCHAR(64)
);

INSERT IGNORE INTO hair_styles (name, label) VALUES
  ('straight', 'Straight'),
  ('wavy',     'Wavy'),
  ('curly',    'Curly'),
  ('coily',    'Coily / Natural'),
  ('bob',      'Bob'),
  ('pixie',    'Pixie Cut'),
  ('lob',      'Long Bob (Lob)'),
  ('layered',  'Layered'),
  ('bangs',    'Bangs / Fringe'),
  ('updo',     'Updo / Bun');

-- ── Pictures ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pictures (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    file_name    VARCHAR(256) NOT NULL UNIQUE,
    file_path    VARCHAR(512),
    file_size    VARCHAR(32),
    height       INT,
    width        INT,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ── Model Pictures ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS model_pictures (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    file_name      VARCHAR(255) NOT NULL UNIQUE,
    file_path      VARCHAR(512),
    file_size      VARCHAR(32),
    height         INT,
    width          INT,
    hair_colour_id INT,
    hair_style_id  INT,
    face_shape_id  INT,
    hair_length_id INT,
    date_created   DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_updated   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hair_colour_id) REFERENCES hair_colours(id) ON DELETE SET NULL,
    FOREIGN KEY (hair_style_id)  REFERENCES hair_styles(id)  ON DELETE SET NULL,
    FOREIGN KEY (face_shape_id)  REFERENCES face_shapes(id)  ON DELETE SET NULL,
    FOREIGN KEY (hair_length_id) REFERENCES hair_lengths(id) ON DELETE SET NULL
);

-- ── History ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS history (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    picture_id          INT,
    original_picture_id INT,
    previous_picture_id INT,
    hair_colour_id      INT,
    hair_style_id       INT,
    face_shape_id       INT,
    user_id             INT,
    date_created        DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_updated        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (picture_id)          REFERENCES pictures(id)     ON DELETE CASCADE,
    FOREIGN KEY (original_picture_id) REFERENCES pictures(id)     ON DELETE SET NULL,
    FOREIGN KEY (previous_picture_id) REFERENCES pictures(id)     ON DELETE SET NULL,
    FOREIGN KEY (hair_colour_id)      REFERENCES hair_colours(id) ON DELETE SET NULL,
    FOREIGN KEY (hair_style_id)       REFERENCES hair_styles(id)  ON DELETE SET NULL,
    FOREIGN KEY (face_shape_id)       REFERENCES face_shapes(id)  ON DELETE SET NULL,
    FOREIGN KEY (user_id)             REFERENCES users(id)        ON DELETE CASCADE
);
