package mainpackage;

public class Lexer {
	
	public static String symComment = "#";
	public static String _comment = "comment";
	public static String _dp = "dp";
	public static String _op = "op";
	public static String _cp = "cp";
	public static String _pc = "pc";
	public static String _br = "br";
	public static String _nl = "nl";
	public static String _id = "id";
	public static String _coma = "coma";
	public static String _tab = "tab";
	public static String _s = "s";
	public static String _measure = "measure";
	public static String _meta = "meta";
	public static String _header = "header";
	public static String _import = "import";
	public static String _define = "define";
	public static String _include = "include";
	public static String _type = "type";
	public static String _name = "name";
	public static String _content = "content";
	public static String _container = "container";
	public static String _itemcont = "itemcont";
	public static String _radiogroup = "radiogroup";
	public static String _radiobutton = "radiobutton";
	public static String _component = "component";
	public static String _item = "item";
	public static String _attr = "attr";
	public static String _bool = "bool";
	public static String _color = "color";
	public static String _font = "font";
	public static String _tdecor = "tdecor";
	public static String _align = "align";
	public static String _effect = "effect";
	public static String _animation = "animation";
	public static String _charset = "charset";
	public static String _text = "text";
	public static String _integer = "integer";
	public static String _real = "real";
	public static String _definetype = "definetype";
	public static String _indicators = "indicators";
	public static String _slidecontrols = "slidecontrols";
	public static String _none = "none";


	public static String[][] lexer = {
			{_comment,			"^(#.*)"},
			{_dp,				"^:"},
			{_op,				""},
			{_cp,				""},
			{_pc,				""},
			{_br,				"^\\s*\\+\\s*\\n\\s*"},
			{_nl,				"^\n"},
			{_coma,				"^,"},
			{_tab,				"^(\\s{4})"},
			{_s,				"\\s{1}"},
			{_measure,			"^((0|[1-9][0-9]*)((px)|(%)))"},
			{_meta,				"^((author)|(keywords)|(lang)|(redirect)|(pageicon)|(title)|(charset)|(description))"},
			{_import,			"^import"},
			{_define,			"^define"},
			{_include,			"^include"},
			{_type,				"^type"},
			{_name,				"^name"},
			{_content,			"^content"},
			{_header,			"^header"},
			{_container,		"^((box)|(vbox)|(hbox)|(sidebox)|(modalbox))"},
			{_itemcont,			"^((tablebox)|(dropdownbox)|(tabbedbox)|(accordionbox)|(slideshow))"},
			{_radiogroup,		"^radiogroup"},
			{_radiobutton,		"^radiobutton"},
			{_component,		"^((button)|(image)|(video)|(audio)|(textfield)|(checkbox)|(label)|(progressbar))"},
			{_item,				"^item"},
			{_attr,				"^((alt)|(poster)|(src)|(autoplay)|(controls)|(loop)|(muted)|(preload)|(onclick)|(onchange)|(alignment)|(type)|(placeholder)|(header)|(text-align)|(text-decoration)|(text-color)|(text)|(font-size)|(font-family)|(animation)|(background-color)|(border-color)|(border-radius)|(border-size)|(border)|(class)|(effect)|(elevation)|(height)|(id)|(link)|(margin)|(padding)|(slots)|(tooltip)|(width)|(closable)|(delay)|(slide-controls)|(indicators)|(caption-position)|(caption))"},
			{_bool,				"^((true)|(false))"},
			{_color,			"^((red)|(pink)|(purple)|(deep-purple)|(indigo)|(blue-gray)|(blue)|(light-blue)|(cyan)|(aqua)|(teal)|(green)|(light-green)|(lime)|(sand)|(khaki)|(yellow)|(amber)|(orange)|(deep-orange)|(brown)|(light-gray)|(gray)|(dark-gray)|(pale-red)|(pale-yellow)|(pale-green)|(pale-blue))"},
			{_font,				"^((TimesNewRoman)|(Georgia)|(AndaleMono)|(ArialBlack)|(Arial)|(Impact)|(TrebuchetMS)|(Verdana)|(ComicSansMS)|(CourierNew))"},
			{_tdecor,			"^((wide)|(bold)|(italic)|(shadowed)|(underlined)|(strikethrough))"},
			{_align,			"^((top-right)|(top-left)|(bottom-right)|(bottom-left)|(center)|(top)|(bottom)|(right)|(left))"},
			{_effect,			"^((opacity-max)|(opacity-min)|(opacity)|(grayscale-max)|(grayscale-min)|(grayscale)|(sepia-max)|(sepia-min)|(sepia))"},
			{_animation,		"^((zoom)|(fading)|(spin)|(move-up)|(move-down)|(move-right)|(move-left))"},
			{_charset,			"^((cs-8859)|(cs-ansi)|(cs-ascii)|(cs-utf-8))"},	
			{_text,				"^((\\\"[^\"]*\\\")|('[^']*'))"},
			{_integer,			"^(0|([1-9][0-9]*))"},
			{_real,				"^((0.[0-9]*[1-9])|([1-9][0-9]*.[0-9]*[1-9]))"},
			{_definetype,		"^((color)|(font)|(tag)|(attr))"},
			{_indicators,	 	"^((dots)|(numbers)|(miniatures))"},
			{_slidecontrols,	"^((angulars-bottom)|(angulars)|(arrows-bottom)|(arrows))"},
			{_none,				"^none"},
			{_id,				"^([a-zA-Z_][a-zA-Z0-9_-])"}};
}


