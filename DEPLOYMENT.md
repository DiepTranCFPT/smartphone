# Deployment Guide

## 🚀 Deploy to Vercel (Not Recommended for Spring Boot)

**Note**: Vercel is not ideal for Spring Boot applications. Consider alternatives like Railway, Heroku, or Docker-based services.

### Steps:
1. Install Vercel CLI:
```bash
npm i -g vercel
```

2. Login to Vercel:
```bash
vercel login
```

3. Deploy:
```bash
vercel --prod
```

## 🚀 Recommended: Deploy to Railway

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

4. Set buildpack:
```bash
heroku buildpacks:set heroku/java
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
docker run -p 8081:8081 diepxdemo
```

### Push to Docker Hub:
```bash
docker tag diepxdemo your-username/diepxdemo
docker push your-username/diepxdemo
```

## 🔧 Environment Variables

For production deployment, set these environment variables:

- `SPRING_PROFILES_ACTIVE=production`
- `PORT=8081` (or your preferred port)
- `DATABASE_URL` (if using external database)
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

## 📋 Health Check

Once deployed, check application health at:
```
https://your-app-url/actuator/health
```
