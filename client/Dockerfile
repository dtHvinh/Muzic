## Build stage
FROM oven/bun:1.2.22 AS builder

WORKDIR /app

# Install deps first (better layer caching)
COPY package.json bun.lockb* bun.lock* ./
RUN bun install --frozen-lockfile

# Build
COPY . .
RUN bun run build


## Runtime stage
FROM nginx:1.27-alpine

# SPA-friendly Nginx config (history fallback)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Static assets
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

# Keep Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
