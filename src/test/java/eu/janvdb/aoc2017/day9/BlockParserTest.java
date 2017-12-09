package eu.janvdb.aoc2017.day9;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockParserTest {

	private BlockParser blockParser = new BlockParser();

	@Test
	public void testGroups1() {
		assertThat(blockParser.countGroups("{}")).isEqualTo(1);
	}

	@Test
	public void testGroups2() {
		assertThat(blockParser.countGroups("{{{}}}")).isEqualTo(6);
	}

	@Test
	public void testGroups3() {
		assertThat(blockParser.countGroups("{{},{}}")).isEqualTo(5);
	}

	@Test
	public void testGroups4() {
		assertThat(blockParser.countGroups("{{{},{},{{}}}}")).isEqualTo(16);
	}

	@Test
	public void testGroups5() {
		assertThat(blockParser.countGroups("{<a>,<a>,<a>,<a>}")).isEqualTo(1);
	}

	@Test
	public void testGroups6() {
		assertThat(blockParser.countGroups("{{<ab>},{<ab>},{<ab>},{<ab>}}")).isEqualTo(9);
	}

	@Test
	public void testGroups7() {
		assertThat(blockParser.countGroups("{{<!!>},{<!!>},{<!!>},{<!!>}}")).isEqualTo(9);
	}

	@Test
	public void testGroups8() {
		assertThat(blockParser.countGroups("{{<!>},{<!>},{<!>},{<a>}}")).isEqualTo(3);
	}


	@Test
	public void testGarbage1() {
		assertThat(blockParser.countGarbage("<>")).isEqualTo(0);
	}

	@Test
	public void testGarbage2() {
		assertThat(blockParser.countGarbage("<random characters>")).isEqualTo(17);
	}

	@Test
	public void testGarbage3() {
		assertThat(blockParser.countGarbage("<<<<>")).isEqualTo(3);
	}

	@Test
	public void testGarbage4() {
		assertThat(blockParser.countGarbage("<{!>}>")).isEqualTo(2);
	}

	@Test
	public void testGarbage5() {
		assertThat(blockParser.countGarbage("<!!>")).isEqualTo(0);
	}

	@Test
	public void testGarbage6() {
		assertThat(blockParser.countGarbage("<!!!>>")).isEqualTo(0);
	}

	@Test
	public void testGarbage7() {
		assertThat(blockParser.countGarbage("<{o\"i!a,<{i<a>")).isEqualTo(10);
	}

}