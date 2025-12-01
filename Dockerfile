# ---- Stage 1: Build environment ----
FROM python:3.11-slim AS builder
WORKDIR /app

RUN apt-get update && apt-get install -y build-essential && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY app/requirements.txt .

# --- create venv and install in it ---
RUN python -m venv /opt/venv && \
    /opt/venv/bin/pip install --upgrade pip && \
    /opt/venv/bin/pip install --no-cache-dir -r requirements.txt

COPY app/ /app

# ---- Stage 2: Production image ----
FROM python:3.11-slim
WORKDIR /app


RUN addgroup --gid 1001 --system nonroot && \
    adduser --no-create-home --shell /bin/false \
    --disabled-password --uid 1001 --system --group nonroot


COPY --from=builder --chown=nonroot:nonroot /app /app
COPY --from=builder --chown=nonroot:nonroot /opt/venv /opt/venv


ENV PATH="/opt/venv/bin:${PATH}"

USER nonroot:nonroot

EXPOSE 5051
CMD ["python", "app.py"]
