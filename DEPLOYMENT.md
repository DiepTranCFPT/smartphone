# Deployment Guide

## 🚀 Deploy to Render (Recommended for Spring Boot)

Render is perfect for Spring Boot applications with Docker support.

### Steps:
1. Push your code to GitHub repository
2. Visit [render.com](https://render.com) and sign up
3. Click "New +" → "Web Service"
4. Connect your GitHub repository
5. Configure the service:
   - **Name**: diepxdemo (or your preferred name)
   - **Environment**: Docker
   - **Region**: Choose closest to your users
   - **Branch**: main
   - **Root Directory**: . (leave blank)
   - **Build Command**: (leave blank, Docker will handle)
   - **Start Command**: (leave blank, Docker will handle)

6. Set Environment Variables (optional):
   - `SPRING_PROFILES_ACTIVE`: production
   - `DATABASE_URL`: (if using external database)

7. Click "Create Web Service"

Render will automatically:
- Detect your Dockerfile
- Build your application
- Deploy to a live URL
- Provide HTTPS automatically
- Auto-deploy on code changes

### Render Benefits:
- ✅ Native Docker support
- ✅ Auto HTTPS/SSL
- ✅ Auto deployments from Git
- ✅ Health checks
- ✅ Logs and monitoring
- ✅ Free tier available

## 🚀 Deploy to Railway

1. Visit [railway.app](https://railway.app)
2. Connect your GitHub repository
3. Railway will automatically detect the Dockerfile and deploy

## 🚀 Deploy to Heroku

1. Install Heroku CLI
2. Login:
```bash
heroku login
```

3. Create app:
```bash
heroku create your-app-name
```

4. Set buildpack for Docker:
```bash
heroku stack:set container
```

5. Deploy:
```bash
git push heroku main
```

## 🐳 Docker Commands

### Build locally:
```bash
docker build -t diepxdemo .
```

### Run locally:
```bash
docker run -p 8081:8081 -e PORT=8081 diepxdemo
```

### Push to Docker Hub:
```bash
docker tag diepxdemo your-username/diepxdemo
docker push your-username/diepxdemo
```

## 🔧 Environment Variables

For production deployment, set these environment variables:

- `SPRING_PROFILES_ACTIVE=production`
- `PORT` (automatically set by Render/Railway/Heroku)
- `DATABASE_URL` (if using external database like PostgreSQL/MySQL)
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

## 📋 Health Check

Once deployed, check application health at:
```
https://your-app-url/actuator/health
```

## 🗃️ Database Options

### Option 1: H2 In-Memory (Default)
- Good for testing and demos
- Data is lost on restart

### Option 2: PostgreSQL on Render
1. In Render dashboard, create new PostgreSQL database
2. Copy the DATABASE_URL
3. Add it as environment variable to your web service

### Option 3: External Database
Set these environment variables:
- `DATABASE_URL`: jdbc:postgresql://host:port/dbname
- `DATABASE_USERNAME`: your_username
- `DATABASE_PASSWORD`: your_password
- `DATABASE_DRIVER`: org.postgresql.Driver
- `HIBERNATE_DIALECT`: org.hibernate.dialect.PostgreSQLDialect
