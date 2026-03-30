# 🏥 VitaGest — Backend API

> API REST sécurisée pour la gestion d'une pharmacie, construite avec **Spring Boot 3**, **Spring Security (JWT)**, et **PostgreSQL**.

---

## 📋 Table des Matières

- [Présentation](#-présentation)
- [Stack Technique](#-stack-technique)
- [Architecture du Projet](#-architecture-du-projet)
- [Prérequis](#-prérequis)
- [Installation & Lancement](#-installation--lancement)
- [Variables d'Environnement](#-variables-denvironnement)
- [Endpoints API](#-endpoints-api)
- [Sécurité & Authentification](#-sécurité--authentification)
- [Docker](#-docker)
- [Base de Données](#-base-de-données)

---

## 📌 Présentation

**VitaGest Backend** est le cœur métier du système de gestion pharmaceutique VitaGest. Il expose une API REST complète permettant de gérer :

- 💊 Le catalogue de médicaments et les lots (stocks)
- 🧾 Les ventes et les lignes de vente
- 🚚 Les commandes fournisseurs
- 👤 Les clients et leurs allergies
- 🏭 Les fournisseurs
- 👥 Les utilisateurs et leurs rôles (Admin / Pharmacien)
- 📋 L'audit log de toutes les actions

---

## 🛠 Stack Technique

| Technologie | Version | Rôle |
|---|---|---|
| **Java** | 17 | Langage principal |
| **Spring Boot** | 3.2.1 | Framework applicatif |
| **Spring Security** | 6.x | Authentification & Autorisation |
| **Spring Data JPA** | 3.x | ORM / Accès base de données |
| **JWT (jjwt)** | 0.11.5 | Tokens d'authentification |
| **PostgreSQL** | 14 | Base de données relationnelle |
| **Lombok** | 1.18.30 | Réduction du code boilerplate |
| **MapStruct** | 1.5.5 | Mapping entités ↔ DTOs |
| **Maven** | 3.8+ | Gestionnaire de build |
| **Docker** | 24+ | Conteneurisation |

---

## 🗂 Architecture du Projet

```
vitagest-backend/
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java        # Point d'entrée Spring Boot
│   ├── auth/                       # Authentification JWT (login, register)
│   ├── config/                     # Configuration Security, CORS, DataInitializer
│   ├── controller/                 # Couche REST (endpoints HTTP)
│   │   ├── AuditLogController.java
│   │   ├── ClientController.java
│   │   ├── CommandeController.java
│   │   ├── FournisseurController.java
│   │   ├── LotController.java
│   │   ├── MedicamentController.java
│   │   ├── RoleController.java
│   │   ├── UserController.java
│   │   └── VenteController.java
│   ├── dto/                        # Data Transfer Objects
│   ├── entity/                     # Entités JPA (tables BDD)
│   │   ├── AuditLog.java
│   │   ├── Client.java
│   │   ├── Commande.java
│   │   ├── CommandeLigne.java
│   │   ├── Fournisseur.java
│   │   ├── Lot.java
│   │   ├── Medicament.java
│   │   ├── Role.java
│   │   ├── User.java
│   │   └── VenteLigne.java
│   ├── exception/                  # Gestion globale des erreurs
│   ├── mapper/                     # Mappers MapStruct
│   ├── repository/                 # Interfaces JPA Repository
│   ├── service/                    # Couche métier (business logic)
│   └── aop/                        # Aspect-Oriented Programming (audit)
├── Dockerfile
├── pom.xml
└── README.md
```

### 🔄 Flux de données

```
Client HTTP → Controller → Service → Repository → PostgreSQL
                   ↕              ↕
                  DTO          Entité JPA
                   ↕
               MapStruct Mapper
```

---

## ✅ Prérequis

- **Java 17+** — [Télécharger](https://adoptium.net/)
- **Maven 3.8+** — [Télécharger](https://maven.apache.org/download.cgi)
- **PostgreSQL 14+** — [Télécharger](https://www.postgresql.org/download/) OU utiliser Docker
- **Docker & Docker Compose** (optionnel, recommandé)

---

## 🚀 Installation & Lancement

### Option 1 — Lancement avec Docker (recommandé)

```bash
# Depuis la racine du projet VitaGest-Project/
docker-compose up --build
```

L'API sera disponible sur : `http://localhost:8080`

---

### Option 2 — Lancement local (sans Docker)

**1. Créer la base de données PostgreSQL :**

```sql
CREATE DATABASE vitagest;
CREATE USER vitagest_user WITH PASSWORD 'vitagest_password';
GRANT ALL PRIVILEGES ON DATABASE vitagest TO vitagest_user;
```

**2. Configurer `application.properties` :**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vitagest
spring.datasource.username=vitagest_user
spring.datasource.password=vitagest_password
spring.jpa.hibernate.ddl-auto=update
```

**3. Compiler et lancer :**

```bash
cd vitagest-backend

# Compiler
./mvnw clean package -DskipTests

# Lancer
./mvnw spring-boot:run
```

---

## 🔐 Variables d'Environnement

| Variable | Valeur par défaut | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/vitagest` | URL de connexion PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `vitagest_user` | Utilisateur BDD |
| `SPRING_DATASOURCE_PASSWORD` | `vitagest_password` | Mot de passe BDD |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Stratégie DDL JPA |
| `JWT_SECRET` | *(défini dans application.properties)* | Clé secrète JWT |

---

## 📡 Endpoints API

### 🔐 Authentification

| Méthode | Endpoint | Description | Auth requise |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Se connecter (retourne JWT) | ❌ |
| `POST` | `/api/v1/auth/register` | Créer un compte | ❌ |

### 👥 Utilisateurs

| Méthode | Endpoint | Description | Rôle requis |
|---|---|---|---|
| `GET` | `/api/v1/users` | Lister tous les utilisateurs | Authentifié |
| `GET` | `/api/v1/users/{id}` | Détail d'un utilisateur | Authentifié |
| `POST` | `/api/v1/users` | Créer un utilisateur | `ADMIN` |
| `PUT` | `/api/v1/users/{id}` | Modifier un utilisateur | `ADMIN` |
| `DELETE` | `/api/v1/users/{id}` | Supprimer un utilisateur | `ADMIN` |
| `PATCH` | `/api/v1/users/{id}/toggle-active` | Activer/Désactiver un compte | `ADMIN` |
| `PATCH` | `/api/v1/users/{id}/reset-password` | Réinitialiser le mot de passe | `ADMIN` |

### 💊 Médicaments

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/medicaments` | Lister tous les médicaments |
| `GET` | `/api/v1/medicaments/{id}` | Détail d'un médicament |
| `POST` | `/api/v1/medicaments` | Ajouter un médicament |
| `PUT` | `/api/v1/medicaments/{id}` | Modifier un médicament |
| `DELETE` | `/api/v1/medicaments/{id}` | Supprimer un médicament |

### 📦 Lots (Stocks)

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/lots` | Lister tous les lots |
| `GET` | `/api/v1/lots/{id}` | Détail d'un lot |
| `GET` | `/api/v1/lots/medicament/{medicamentId}` | Lots d'un médicament spécifique |
| `POST` | `/api/v1/lots` | Ajouter un lot |
| `PUT` | `/api/v1/lots/{id}` | Modifier un lot |
| `DELETE` | `/api/v1/lots/{id}` | Supprimer un lot |

### 🧾 Ventes

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/ventes` | Lister toutes les ventes |
| `GET` | `/api/v1/ventes/{id}` | Détail d'une vente |
| `POST` | `/api/v1/ventes` | Créer une vente |
| `DELETE` | `/api/v1/ventes/{id}` | Annuler une vente |

### 🚚 Commandes Fournisseurs

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/commandes` | Lister toutes les commandes |
| `GET` | `/api/v1/commandes/{id}` | Détail d'une commande |
| `POST` | `/api/v1/commandes` | Créer une commande |
| `PUT` | `/api/v1/commandes/{id}` | Modifier une commande |
| `POST` | `/api/v1/commandes/{id}/valider` | Valider une commande |
| `POST` | `/api/v1/commandes/{id}/reception` | Réceptionner une commande |
| `DELETE` | `/api/v1/commandes/{id}` | Supprimer une commande |

### 👤 Clients

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/clients` | Lister tous les clients |
| `GET` | `/api/v1/clients/{id}` | Détail d'un client |
| `POST` | `/api/v1/clients` | Ajouter un client |
| `PUT` | `/api/v1/clients/{id}` | Modifier un client |
| `DELETE` | `/api/v1/clients/{id}` | Supprimer un client |

### 🏭 Fournisseurs

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/fournisseurs` | Lister tous les fournisseurs |
| `GET` | `/api/v1/fournisseurs/{id}` | Détail d'un fournisseur |
| `POST` | `/api/v1/fournisseurs` | Ajouter un fournisseur |
| `PUT` | `/api/v1/fournisseurs/{id}` | Modifier un fournisseur |
| `DELETE` | `/api/v1/fournisseurs/{id}` | Supprimer un fournisseur |

### 📋 Audit Logs

| Méthode | Endpoint | Description | Rôle requis |
|---|---|---|---|
| `GET` | `/api/v1/audit-logs` | Lister tous les logs | `ADMIN` |
| `GET` | `/api/v1/audit-logs/{id}` | Détail d'un log | `ADMIN` |

---

## 🔒 Sécurité & Authentification

Le backend utilise **Spring Security** avec des **tokens JWT** (JSON Web Tokens).

### Flux d'authentification

```
1. POST /api/v1/auth/login  →  { email, password }
2. Serveur vérifie les credentials
3. Retourne un JWT token
4. Client inclut le token dans chaque requête :
   Authorization: Bearer <token>
```

### Rôles disponibles

| Rôle | Privilèges |
|---|---|
| `ROLE_ADMIN` | Gestion des utilisateurs, accès aux audit logs, lecture globale |
| `ROLE_PHARMACIEN` | Gestion complète des ventes, médicaments, stocks, commandes, clients |

---

## 🐳 Docker

### Build de l'image seule

```bash
cd vitagest-backend
docker build -t vitagest-backend .
```

### Lancer avec variables d'environnement

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/vitagest \
  -e SPRING_DATASOURCE_USERNAME=vitagest_user \
  -e SPRING_DATASOURCE_PASSWORD=vitagest_password \
  vitagest-backend
```

### Multi-stage build

Le `Dockerfile` utilise un **build multi-stage** :
- **Stage 1** (`maven:3.8.5-openjdk-17`) : Compilation du projet avec Maven
- **Stage 2** (`eclipse-temurin:17-jre-alpine`) : Image légère pour l'exécution uniquement

---

## 🗃 Base de Données

### Schéma des tables principales

```
users ──────────────── role
  │
  │ (vendeur)
  ▼
vente ──── vente_ligne ──── medicament ──── lot
  │
  ▼
client

commande ──── commande_ligne ──── medicament
  │
  ▼
fournisseur
```

### Données initiales (DataInitializer)

Au démarrage de l'application, un `DataInitializer` crée automatiquement :
- Les rôles `ROLE_ADMIN` et `ROLE_PHARMACIEN`
- Un compte administrateur par défaut

---

## 👨‍💻 Auteur

Projet réalisé dans le cadre de la formation **YouCode** (Fil Rouge).

---

*Version : 0.0.1-SNAPSHOT | Java 17 | Spring Boot 3.2.1*