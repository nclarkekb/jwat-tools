package org.jwat.tools;

import java.util.HashMap;
import java.util.Map;

import org.jwat.tools.core.CommandLine;
import org.jwat.tools.core.CommandLine.Argument;
import org.jwat.tools.core.Task;
import org.jwat.tools.tasks.ConvertTask;
import org.jwat.tools.tasks.IntervalTask;
import org.jwat.tools.tasks.PathIndexTask;
import org.jwat.tools.tasks.UnpackTask;
import org.jwat.tools.tasks.cdx.CDXTask;
import org.jwat.tools.tasks.compress.CompressTask;
import org.jwat.tools.tasks.decompress.DecompressTask;
import org.jwat.tools.tasks.extract.ExtractTask;
import org.jwat.tools.tasks.test.TestTask;

public class JWATTools {

	public static final int A_COMMAND = 1;
	public static final int A_FILES = 2;
	public static final int A_WORKERS = 3;
	public static final int A_COMPRESS = 4;
	public static final int A_SHOW_ERRORS = 5;
	public static final int A_RECURSIVE = 6;
	public static final int A_XML = 7;
	public static final int A_LAX = 8;

	public static void main(String[] args) {
		JWATTools tools = new JWATTools();
		tools.Main( args );
	}

	protected static Map<String, Class<? extends Task>> commands = new HashMap<String, Class<? extends Task>>();

	static {
		commands.put("arc2warc", ConvertTask.class);
		commands.put("cdx", CDXTask.class);
		commands.put("compress", DecompressTask.class);
		commands.put("decompress", CompressTask.class);
		commands.put("extract", ExtractTask.class);
		commands.put("interval", IntervalTask.class);
		commands.put("pathindex", PathIndexTask.class);
		commands.put("test", TestTask.class);
		commands.put("unpack", UnpackTask.class);
	}

	public void Main(String[] args) {
		CommandLine.Arguments arguments = null;
		CommandLine cmdLine = new CommandLine();
		cmdLine.addListArgument( "command", A_COMMAND, 1, 1 );
		cmdLine.addOption( "-1", A_COMPRESS, 1 );
		cmdLine.addOption( "-2", A_COMPRESS, 2 );
		cmdLine.addOption( "-3", A_COMPRESS, 3 );
		cmdLine.addOption( "-4", A_COMPRESS, 4 );
		cmdLine.addOption( "-5", A_COMPRESS, 5 );
		cmdLine.addOption( "-6", A_COMPRESS, 6 );
		cmdLine.addOption( "-7", A_COMPRESS, 7 );
		cmdLine.addOption( "-8", A_COMPRESS, 8 );
		cmdLine.addOption( "-9", A_COMPRESS, 9 );
		cmdLine.addOption( "--fast", A_COMPRESS, 1 );
		cmdLine.addOption( "--best", A_COMPRESS, 9 );
		cmdLine.addOption( "-e", A_SHOW_ERRORS );
		cmdLine.addOption( "-l", A_LAX );
		cmdLine.addOption( "-r", A_RECURSIVE );
		cmdLine.addOption( "-w=", A_WORKERS );
		cmdLine.addOption( "-x", A_XML );
		cmdLine.addListArgument( "files", A_FILES, 1, Integer.MAX_VALUE );

		try {
			arguments = cmdLine.parse( args );
			/*
			for ( int i=0; i<arguments.switchArgsList.size(); ++i) {
				argument = arguments.switchArgsList.get( i );
				System.out.println( argument.argDef.id + "," + argument.argDef.subId + "=" + argument.value );
			}
			*/
		}
		catch (CommandLine.ParseException e) {
			System.out.println( getClass().getName() + ": " + e.getMessage() );
			System.exit( 1 );
		}

		if ( arguments == null ) {
			System.out.println( "JWATTools v0.5.5" );
			System.out.println( "usage: JWATTools [-dte19] command [file ...]" );
			System.out.println( "" );
			System.out.println( "Commands:" );
			System.out.println( "   arc2warc     convert ARC to WARC");
			System.out.println( "   cdx          create a CDX index (unsorted)");
			System.out.println( "   compress     compress");
			System.out.println( "   decompress   decompress");
			System.out.println( "   extract      extract ARC/WARC record(s)");
			System.out.println( "   interval     interval extract");
			System.out.println( "   pathindex    create a heritrix path index (unsorted)");
			System.out.println( "   test         test validity of ARC/WARC/GZip file(s)");
			System.out.println( "   unpack       unpack multifile GZip");
			System.out.println( "" );
			System.out.println( "Options:" );
			System.out.println( "   -r      recursive (currently has no effect)" );
			System.out.println( "   -w<x>   set the amount of worker thread(s) (defaults to 1)" );
			System.out.println( "" );
			System.out.println( "Test options:" );
			System.out.println( "   -e   show errors" );
			System.out.println( "   -l   relaxed URL URI validation" );
			System.out.println( "   -x   to validate text/xml payload (eg. mets)" );
			System.out.println( "" );
			System.out.println( "Compress options:" );
			System.out.println( "   -1, --fast   compress faster" );
			System.out.println( "   -9, --slow   compress better" );
			System.out.println( "" );
		}
		else {
			Argument argument = arguments.idMap.get( JWATTools.A_COMMAND );
			String commandStr = argument.value.toLowerCase();

			Class<? extends Task> clazz = commands.get(commandStr);
			if (clazz != null) {
				try {
					Task task = clazz.newInstance();
					task.command(arguments);
				}
				catch (InstantiationException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Unknown command -- " + commandStr);
			}
		}
	}

}
