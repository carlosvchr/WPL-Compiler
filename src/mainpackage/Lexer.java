package mainpackage;

public class Lexer {
	
	public static final String symComment = "#";
	public static final String _comment = "comment";
	public static final String _dp = "dp";
	public static final String _op = "op";
	public static final String _cp = "cp";
	public static final String _pc = "pc";
	public static final String _br = "br";
	public static final String _nl = "nl";
	public static final String _id = "id";
	public static final String _coma = "coma";
	public static final String _tab = "tab";
	public static final String _s = "s";
	public static final String _measure = "measure";
	public static final String _meta = "meta";
	public static final String _header = "header";
	public static final String _import = "import";
	public static final String _define = "define";
	public static final String _include = "include";
	public static final String _type = "type";
	public static final String _name = "name";
	public static final String _content = "content";
	public static final String _container = "container";
	public static final String _itemcont = "itemcont";
	public static final String _radiogroup = "radiogroup";
	public static final String _radiobutton = "radiobutton";
	public static final String _component = "component";
	public static final String _item = "item";
	public static final String _attr = "attr";
	public static final String _bool = "bool";
	//public static final String _color = "color";
	public static final String _font = "font";
	public static final String _tdecor = "tdecor";
	public static final String _align = "align";
	public static final String _effect = "effect";
	public static final String _animation = "animation";
	public static final String _charset = "charset";
	public static final String _text = "text";
	public static final String _integer = "integer";
	public static final String _real = "real";
	public static final String _indicators = "indicators";
	public static final String _slidecontrols = "slidecontrols";
	public static final String _none = "none";
	public static final String _author = "author";
	public static final String _keywords = "keywords";
	public static final String _lang = "lang";
	public static final String _redirect = "redirect";
	public static final String _pageicon = "pageicon";
	public static final String _title = "title";
	public static final String _description = "description";
	public static final String _box = "box";
	public static final String _vbox = "vbox";
	public static final String _hbox = "hbox";
	public static final String _sidebox = "sidebox";
	public static final String _modalbox = "modalbox";
	public static final String _tablebox = "tablebox";
	public static final String _dropdownbox = "dropdownbox";
	public static final String _tabbedbox = "tabbedbox";
	public static final String _accordionbox = "accordionbox";
	public static final String _slideshow = "slideshow";
	public static final String _button = "button";
	public static final String _image = "image";
	public static final String _video = "video";
	public static final String _audio = "audio";
	public static final String _textfield = "textfield";
	public static final String _checkbox = "checkbox";
	public static final String _label = "label";
	public static final String _progressbar = "progressbar";
	public static final String _true = "true";
	public static final String _false = "false";
	public static final String _bgcolor = "background-color";
	public static final String _border = "border";
	public static final String _border_color = "border-color";
	public static final String _border_radius = "border-radius";
	public static final String _class = "class";
	public static final String _elevation = "elevation";
	public static final String _height = "height";
	public static final String _link = "link";
	public static final String _margin = "margin";
	public static final String _padding = "padding";
	public static final String _slots = "slots";
	public static final String _tooltip = "tooltip";
	public static final String _width = "width";
	public static final String _closable = "closable";
	public static final String _alt = "alt";
	public static final String _poster = "poster";
	public static final String _src = "src";
	public static final String _autoplay = "autoplay";
	public static final String _controls = "controls";
	public static final String _loop = "loop";
	public static final String _muted = "muted";
	public static final String _preload = "preload";
	public static final String _onclick = "onclick";
	public static final String _onchange = "onchange";
	public static final String _placeholder = "placeholder";
	public static final String _text_align = "text-align";
	public static final String _text_decoration = "text-decoration";
	public static final String _text_color = "text-color";
	public static final String _font_family = "font-family";
	public static final String _font_size = "font-size";
	public static final String _delay = "delay";
	public static final String _slide_controls = "slide-controls";
	public static final String _caption_position = "caption-position";
	public static final String _caption = "caption";
	public static final String _selected = "selected";
	
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
			{_meta,				"^(("+_author+")|("+_keywords+")|("+_lang+")|("+_redirect+")|("+_pageicon+")|"+
									"("+_title+")|("+_charset+")|("+_description+"))"},
			{_import,			"^"+_import},
			{_define,			"^"+_define},
			{_include,			"^"+_include},
			{_type,				"^"+_type},
			{_name,				"^"+_name},
			{_content,			"^"+_content},
			{_header,			"^"+_header},
			{_container,		"^(("+_box+")|("+_vbox+")|("+_hbox+")|("+_sidebox+")|("+_modalbox+"))"},
			{_itemcont,			"^(("+_tablebox+")|("+_dropdownbox+")|("+_tabbedbox+")|("+_accordionbox+")|("+_slideshow+"))"},
			{_radiogroup,		"^"+_radiogroup},
			{_radiobutton,		"^"+_radiobutton},
			{_component,		"^(("+_button+")|("+_image+")|("+_video+")|("+_audio+")|("+_textfield+")|("+_checkbox+")|("+_label+")|("+_progressbar+"))"},
			{_item,				"^"+_item},
			{_attr,				"^(("+_alt+")|("+_poster+")|("+_src+")|("+_autoplay+")|("+_controls+")|("+_loop+")|("+_muted+")|("+_preload+")|("+_onclick+")|("+_onchange+")|("+_align+")|("+_type+")|("+_placeholder+")|("+_header+")|("+_text_align+")|("+_text_decoration+")|("+_text_color+")|("+_text+")|("+_font_size+")|("+_font_family+")|("+_animation+")|("+_bgcolor+")|("+_border_color+")|("+_border_radius+")|("+_border+")|("+_class+")|("+_effect+")|("+_elevation+")|("+_height+")|("+_id+")|("+_link+")|("+_margin+")|("+_padding+")|("+_slots+")|("+_tooltip+")|("+_width+")|("+_closable+")|("+_delay+")|("+_slide_controls+")|("+_indicators+")|("+_caption_position+")|("+_caption+")|("+_selected+"))"},
			{_bool,				"^(("+_true+")|("+_false+"))"},
			//{_color,			"^((red)|(pink)|(purple)|(deep-purple)|(indigo)|(blue-gray)|(blue)|(light-blue)|(cyan)|(aqua)|(teal)|(green)|(light-green)|(lime)|(sand)|(khaki)|(yellow)|(amber)|(orange)|(deep-orange)|(brown)|(light-gray)|(gray)|(dark-gray)|(pale-red)|(pale-yellow)|(pale-green)|(pale-blue))"},
			{_font,				"^((TimesNewRoman)|(Georgia)|(AndaleMono)|(ArialBlack)|(Arial)|(Impact)|(TrebuchetMS)|(Verdana)|(ComicSansMS)|(CourierNew))"},
			{_tdecor,			"^((wide)|(bold)|(italic)|(shadowed)|(underlined)|(strikethrough))"},
			{_align,			"^((top-right)|(top-left)|(bottom-right)|(bottom-left)|(center)|(top)|(bottom)|(right)|(left))"},
			{_effect,			"^((opacity-max)|(opacity-min)|(opacity)|(grayscale-max)|(grayscale-min)|(grayscale)|(sepia-max)|(sepia-min)|(sepia))"},
			{_animation,		"^((zoom)|(fading)|(spin)|(move-up)|(move-down)|(move-right)|(move-left))"},
			{_charset,			"^((cs-8859)|(cs-ansi)|(cs-ascii)|(cs-utf-8))"},	
			{_text,				"^((\"[^\"]*\")|('[^']*'))"},
			{_integer,			"^(0|([1-9][0-9]*))"},
			{_real,				"^((0.[0-9]*[1-9])|([1-9][0-9]*.[0-9]*[1-9]))"},
			{_indicators,	 	"^((dots)|(numbers)|(miniatures))"},
			{_slidecontrols,	"^((angulars-bottom)|(angulars)|(arrows-bottom)|(arrows))"},
			{_none,				"^none"},
			{_id,				"^([a-zA-Z_][a-zA-Z0-9_-])"}};
}

// box: alignment, animation, background-color, border, border-color, border-radius, class, effect, elevation, id,
// height, link, margin, padding, slots, tooltip, width, closable

