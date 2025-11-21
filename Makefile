.PHONY: build run stop clean test

build:
	@echo "Building application..."
	mvn clean package -DskipTests

run:
	@echo "Starting application with Docker Compose..."
	docker-compose up --build

stop:
	@echo "Stopping application..."
	docker-compose down

clean:
	@echo "Cleaning up..."
	docker-compose down -v
	mvn clean

test:
	@echo "Running tests..."
	mvn test

logs:
	@echo "Showing logs..."
	docker-compose logs -f app
