package edu.kit.ipd.creativecrowd.view;


import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * Guarantees that only one Configuration object is created, as the creation of Configuration objects is costly
 * 
 * @author simon
 */
public final class FreemarkerConfig {
	private static Configuration cfg = null;

	private FreemarkerConfig() {

	}

	/**
	 * set the Freemarker configuration or return the normal one, if it has been set already
	 * 
	 * @return the current Freemarker configuration
	 */
	public static Configuration getConfig() {
		if (cfg != null) {
			return cfg;
		} else {
			cfg = new Configuration(Configuration.VERSION_2_3_21);
			// Specify the source where the template files come from. Here I set a
			// plain directory for it, but non-file-system sources are possible too:
			// try{
			// cfg.setDirectoryForTemplateLoading(new File("/templates"));
			// } catch (IOException exc) {
			// Logger.logException(exc.getMessage());
			// }

			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			cfg.setDefaultEncoding("UTF-8");

			// Sets how errors will appear.
			// During development TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			// RETHROW_HANDLER
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

			cfg.setClassForTemplateLoading(FreemarkerConfig.class, "/templates/");
		}
		return cfg;
	}
}
