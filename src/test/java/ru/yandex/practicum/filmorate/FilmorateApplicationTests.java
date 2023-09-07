package ru.yandex.practicum.filmorate;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

	HttpClient client;
	ConfigurableApplicationContext context;
	Gson gson;

	@BeforeEach
	void run() {
		context = SpringApplication.run(FilmorateApplication.class, "");
		client = HttpClient.newHttpClient();
		gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.serializeNulls()
				.create();
	}

	@AfterEach
	void stop() {
		context.close();
	}

	@Test
	void shouldReturnUserJsonFromResponse() throws IOException, InterruptedException {
		User user = new User("email@email.com",
				"login",
				"name",
				LocalDate.of(1990, 1,1));

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		String body = response.body();
		User user1 = gson.fromJson(JsonParser.parseString(body).getAsJsonObject(), User.class);

		assertEquals(200, response.statusCode());
		assertEquals(user, user1);
	}

	@Test
	void shouldReturnNameEqualsLogin() throws IOException, InterruptedException {
		User user = new User("email@email.com",
				"login",
				"",
				LocalDate.of(1990, 1,1));

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		String body = response.body();
		User user1 = gson.fromJson(JsonParser.parseString(body).getAsJsonObject(), User.class);

		User userLogin = new User("email@email.com",
				"login",
				"login",
				LocalDate.of(1990, 1,1));

		assertEquals(200, response.statusCode());
		assertEquals(userLogin, user1);
	}

	@Test
	void shouldReturnStatusCode400ByWrongEmail() throws IOException, InterruptedException {
		User user = new User("это-неправильный?эмейл@",
				"login",
				"name",
				LocalDate.of(1990, 1,1));

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByWrongBirthDay() throws IOException, InterruptedException {
		User user = new User("email@email.com",
				"login",
				"name",
				LocalDate.of(2222, 1,1));

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByWrongLogin() throws IOException, InterruptedException {
		User user = new User("email@email.com",
				"login with whitespaces",
				"name",
				LocalDate.of(1990, 1,1));

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

    @Test
	void shouldReturnUpdateUserJsonFromResponse() throws IOException, InterruptedException {

		User user = new User("email@email.com",
				"login",
				"name",
				LocalDate.of(1990, 1,1));

		User userToUpdate = new User("newemail@email.com",
				"newlogin",
				"newname",
				LocalDate.of(1990, 1,1));
		userToUpdate.setId(1);

		URI uri = URI.create("http://localhost:8080/users");

		HttpRequest requestPost = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

		String body = response.body();
		User newUser = gson.fromJson(JsonParser.parseString(body).getAsJsonObject(), User.class);

		System.out.println("создаваемый" + user);

		System.out.println("созданный" + newUser);

		assertEquals(user, newUser);
		assertEquals(200, response.statusCode());

		HttpRequest requestPut = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(userToUpdate)))
				.build();

		HttpResponse<String> response2 = client.send(requestPut, HttpResponse.BodyHandlers.ofString());

		String body2 = response2.body();
		User updateUser = gson.fromJson(JsonParser.parseString(body2).getAsJsonObject(), User.class);

		assertEquals(userToUpdate, updateUser);
		assertEquals(200, response2.statusCode());
	}

	@Test
	void shouldStatusCode400ByWrongUser() throws IOException, InterruptedException {

		User user = new User("email@email.com",
				"login",
				"name",
				LocalDate.of(1990, 1,1));

		User userToUpdate = new User("newemail@email.com",
				"newlogin",
				"newname",
				LocalDate.of(1990, 1,1));
		userToUpdate.setId(9999);

		URI uri = URI.create("http://localhost:8080/users");

		HttpRequest requestPost = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
				.build();

		client.send(requestPost, HttpResponse.BodyHandlers.ofString());

		HttpRequest requestPut = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(userToUpdate)))
				.build();

		HttpResponse<String> response = client.send(requestPut, HttpResponse.BodyHandlers.ofString());

		assertEquals(500, response.statusCode());
	}

	@Test
	void shouldReturnFilmJsonFromResponse() throws IOException, InterruptedException {
		Film film = new Film("name",
				"description",
				LocalDate.of(1990, 1,1),
				90);

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		String body = response.body();
		Film film1 = gson.fromJson(JsonParser.parseString(body).getAsJsonObject(), Film.class);

		assertEquals(200, response.statusCode());
		assertEquals(film, film1);
	}

	@Test
	void shouldReturnStatusCode400ByWrongName() throws IOException, InterruptedException {
		Film film = new Film("   ",
				"description",
				LocalDate.of(1990, 1,1),
				90);

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByWronSizeOfDescription() throws IOException, InterruptedException {
		Film film = new Film("name",
				"descriptionSizeOfMoreThan200chars-descriptionSizeOfMoreThan200chars" +
						"descriptionSizeOfMoreThan200chars-descriptionSizeOfMoreThan200chars" +
						"descriptionSizeOfMoreThan200chars-descriptionSizeOfMoreThan200chars" +
						"descriptionSizeOfMoreThan200chars-descriptionSizeOfMoreThan200chars",
				LocalDate.of(1990, 1,1),
				90);

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByWrongReleaseDate() throws IOException, InterruptedException {
		Film film = new Film("name",
				"description",
				LocalDate.of(1890, 1,1),
				90);

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByWrongDuration() throws IOException, InterruptedException {
		Film film = new Film("name",
				"description",
				LocalDate.of(1890, 1,1),
				-90);

		URI uri = URI.create("http://localhost:8080/films");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnUpdateFilmJsonFromResponse() throws IOException, InterruptedException {

		Film film = new Film("name",
				"description",
				LocalDate.of(1990, 1,1),
				90);

		Film filmToUpdate = new Film("name",
				"description",
				LocalDate.of(1990, 1,1),
				90);
		filmToUpdate.setId(1);

		URI uri = URI.create("http://localhost:8080/films");

		HttpRequest requestPost = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		client.send(requestPost, HttpResponse.BodyHandlers.ofString());

		HttpRequest requestPut = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmToUpdate)))
				.build();

		HttpResponse<String> response = client.send(requestPut, HttpResponse.BodyHandlers.ofString());

		String body = response.body();
		Film updateFilm = gson.fromJson(JsonParser.parseString(body).getAsJsonObject(), Film.class);

		assertEquals(200, response.statusCode());
		assertEquals(filmToUpdate, updateFilm);
	}

	@Test
	void shouldStatusCode400ByWrongFilm() throws IOException, InterruptedException {

		Film film = new Film("name",
				"description",
				LocalDate.of(1890, 1,1),
				90);

		Film filmToUpdate = new Film("name",
				"description",
				LocalDate.of(1890, 1,1),
				90);
		filmToUpdate.setId(9999);

		URI uri = URI.create("http://localhost:8080/films");

		HttpRequest requestPost = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
				.build();

		client.send(requestPost, HttpResponse.BodyHandlers.ofString());

		HttpRequest requestPut = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmToUpdate)))
				.build();

		HttpResponse<String> response = client.send(requestPut, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByEmptyPostRequest() throws IOException, InterruptedException {

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(" ")))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}

	@Test
	void shouldReturnStatusCode400ByEmptyPutRequest() throws IOException, InterruptedException {

		URI uri = URI.create("http://localhost:8080/users");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.setHeader("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(" ")))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(400, response.statusCode());
	}



	static class LocalDateAdapter extends TypeAdapter<LocalDate> {
		private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		@Override
		public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
			jsonWriter.value(localDate.format(formatter));
		}

		@Override
		public LocalDate read(final JsonReader jsonReader) throws IOException {
			return LocalDate.parse(jsonReader.nextString(), formatter);
		}
	}

}






