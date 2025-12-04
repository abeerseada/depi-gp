# ---- Stage 1: Build environment ----
FROM python:3.11-slim AS builder
WORKDIR /app

RUN apt-get update && apt-get install -y build-essential && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY ./requirements.txt .

# --- create venv and install in it ---
RUN pip install --upgrade pip && \
    pip install --no-cache-dir --target=/install -r requirements.txt

COPY . /app

# ---- Stage 2: Production image ----
FROM python:3.11-slim
WORKDIR /app


RUN addgroup --gid 1001 --system nonroot && \
    adduser --no-create-home --shell /bin/false \
    --disabled-password --uid 1001 --system --group nonroot

COPY --from=builder --chown=nonroot:nonroot /install /usr/local/lib/python3.11/site-packages
COPY --from=builder --chown=nonroot:nonroot /app /app

USER nonroot:nonroot

EXPOSE 5051
CMD ["python", "app.py"]

