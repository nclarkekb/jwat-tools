package org.jwat.tools.tasks.decompress;

import org.jwat.tools.JWATTools;

import com.antiaction.common.cli.Argument;
import com.antiaction.common.cli.ArgumentParserException;
import com.antiaction.common.cli.ArgumentParser;
import com.antiaction.common.cli.CommandLine;
import com.antiaction.common.cli.Options;

public class DecompressTaskCLIParser {

	protected DecompressTaskCLIParser() {
	}

	public static DecompressOptions parseArguments(CommandLine cmdLine) {
		Options cliOptions = new Options();
		try {
			cliOptions.addOption(null, "--queue-first", JWATTools.A_QUEUE_FIRST, 0, null);
			cliOptions.addOption("-w", "--workers", JWATTools.A_WORKERS, 0, null).setValueRequired();
			cliOptions.addNamedArgument("files", JWATTools.A_FILES, 1, Integer.MAX_VALUE);
			cmdLine = ArgumentParser.parse(cmdLine.argsArray, cliOptions, cmdLine);
		}
		catch (ArgumentParserException e) {
			System.out.println( DecompressTaskCLIParser.class.getName() + ": " + e.getMessage() );
			System.exit( 1 );
		}

		DecompressOptions options = new DecompressOptions();

		Argument argument;

		// Queue first.
		options.bQueueFirst = cmdLine.idMap.containsKey(JWATTools.A_QUEUE_FIRST);

		// Thread workers.
		argument = cmdLine.idMap.get( JWATTools.A_WORKERS );
		if ( argument != null && argument.value != null ) {
			try {
				options.threads = Integer.parseInt(argument.value);
			} catch (NumberFormatException e) {
				System.out.println( "Invalid number of threads requested: " + argument.value );
				System.exit( 1 );
			}
		}
		if ( options.threads < 1 ) {
			System.out.println( "Invalid number of threads requested: " + options.threads );
			System.exit( 1 );
		}

		// Files.
		argument = cmdLine.idMap.get( JWATTools.A_FILES );
		options.filesList = argument.values;

		return options;
	}

}
