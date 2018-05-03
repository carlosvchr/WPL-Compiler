package mainpackage;

public class Lexer {
	
	// Custom
	public static final String __br= " \\";
	public static final String __end = "end";
	public static final String __blank = "blank";
	public static final String __comment = "comment";
	public static final String __var = "var";
	public static final String __measure = "measure";
	public static final String __integer = "integer";
	public static final String __identifier = "identifier";
	public static final String __text = "text";
	public static final String __color = "color";
	public static final String __dp = "dp";
	public static final String __pc = "pc";
	public static final String __op = "op";
	public static final String __cp = "cp";
	public static final String __meta = "meta";
	public static final String __container = "container";
	public static final String __component = "component";
	public static final String __attr = "attr";
	public static final String __val = "val";
	public static final String __bool = "bool";
	
	// Containers and special tags
	public static final String _define = "define";
	public static final String _import = "import";
	public static final String _include = "include";
	public static final String _accordion = "accordion";
	public static final String _dropdown = "dropdown";
	public static final String _hbox = "hbox";
	public static final String _modal = "modal";
	public static final String _sidebar = "sidebar";
	public static final String _table = "table";
	public static final String _hrow = "hrow";
	public static final String _row = "row";
	public static final String _vbox = "vbox";
	
	// Components
	public static final String _audio = "audio";
	public static final String _button = "button";
	public static final String _checkbox = "checkbox";
	public static final String _image = "image";
	public static final String _label = "label";
	public static final String _radiobutton = "radiobutton";
	public static final String _textfield = "textfield";
	public static final String _video = "video";
	
	// Attributes
	public static final String _align = "align";
	public static final String _alt = "alt";
	public static final String _animation = "animation";
	public static final String _author = "author";
	public static final String _autoplay = "autoplay";
	public static final String _bgcolor = "bgcolor";
	public static final String _border = "border";
	public static final String _bordercolor = "border-color";
	public static final String _borderradius = "border-radius";
	public static final String _charset = "charset";
	public static final String _collapsible = "collapsible";
	public static final String _class = "class";
	public static final String _controls = "controls";
	public static final String _description = "description";
	public static final String _dropdowntype = "dropdown-type";
	public static final String _effect = "effect";
	public static final String _elevation = "elevation";
	public static final String _fontfamily = "font-family";
	public static final String _fontsize = "font-size";
	public static final String _height = "height";
	public static final String _id = "id";
	public static final String _keywords = "keywords";
	public static final String _lang = "lang";
	public static final String _link = "link";
	public static final String _loop = "loop";
	public static final String _margin = "margin";
	public static final String _muted = "muted";
	public static final String _onchange = "onchange";
	public static final String _onclick = "onclick";
	public static final String _padding = "padding";
	public static final String _pageicon = "pageicon";
	public static final String _placeholder = "placeholder";
	public static final String _poster = "poster";
	public static final String _preload = "preload";
	public static final String _radiogroup = "radiogroup";
	public static final String _redirect = "redirect";
	public static final String _selected = "selected";
	public static final String _sidebartype = "sidebar-type";
	public static final String _src = "src";
	public static final String _tableattrs = "table-attrs";
	public static final String _textalign = "textalign";
	public static final String _textcolor = "text-color";
	public static final String _textdecoration = "text-decoration";
	public static final String _title = "title";
	public static final String _tooltip = "tooltip";
	public static final String _width = "width";
	
	// Values
	public static final String _bold = "bold";
	public static final String _bordered = "bordered";
	public static final String _bottom = "bottom";
	public static final String _bottomleft = "bottom-left";
	public static final String _bottomright = "bottom-right";
	public static final String _clickable = "clickable";
	public static final String _center = "center";
	public static final String _centered = "centered";
	public static final String _fadein = "fade-in";
	public static final String _fading = "fading";
	public static final String _false = "false";
	public static final String _floating = "floating";
	public static final String _grayscale = "grayscale";
	public static final String _grayscalemax = "grayscale-max";
	public static final String _grayscalemin = "grayscale-min";
	public static final String _hoverable = "hoverable";
	public static final String _italic = "italic";
	public static final String _left = "left";
	public static final String _movedown = "move-down";
	public static final String _moveleft = "move-left";
	public static final String _moveright = "move-right";
	public static final String _moveup = "move-up";
	public static final String _opacity = "opacity";
	public static final String _opacitymax = "opacity-max";
	public static final String _opacitymin = "opacity-min";
	public static final String _overline = "overline";
	public static final String _right = "right";
	public static final String _sepia = "sepia";
	public static final String _sepiamax = "sepia-max";
	public static final String _sepiamin = "sepia-min";
	public static final String _sliding = "sliding";
	public static final String _spin = "spin";
	public static final String _strikethrough = "strikethrough";
	public static final String _striped = "striped";
	public static final String _top = "top";
	public static final String _topleft = "top-left";
	public static final String _topright = "top-right";
	public static final String _true = "true";
	public static final String _underline = "underline";
	public static final String _zoom = "zoom";
	
	public static String[][] lexer = {
			{__blank,			"^\\s"},
			{__comment,			"^(--.*)"},
			{__dp,				"^:"},	
			{_import,			"^("+_import+")"},			
			{__meta,			"^(("+_author+")|("+_keywords+")|("+_lang+")|("+_redirect+")|("+_pageicon+")|"+
								"("+_title+")|("+_charset+")|("+_description+"))"},			
			{_define,			"^("+_define+")"},			
			{_include,			"^("+_include+")"},	
			{__container,		"^(("+_accordion+")|("+_dropdown+")|("+_hbox+")|("+_modal+")|("+_sidebar+")|"+
								"("+_vbox+"))"},
			{_table,			"^("+_table+")"},
			{_row,				"^("+_row+")"},
			{_hrow,				"^("+_hrow+")"},
			{__component,		"^(("+_audio+")|("+_button+")|("+_checkbox+")|("+_image+")|("+_label+")|"+
								"("+_radiobutton+")|("+_textfield+")|("+_video+"))"},			
			{__attr,			"^(("+_align+")|("+_alt+")|("+_animation+")|("+_author+")|("+_autoplay+")|("+_collapsible+")|"+
								"("+_bgcolor+")|("+_bordercolor+")|("+_borderradius+")|("+_border+")|("+_charset+")|"+
								"("+_class+")|("+_controls+")|("+_description+")|("+_dropdowntype+")|("+_effect+")|"+
								"("+_elevation+")|("+_fontfamily+")|("+_fontsize+")|("+_height+")|("+_id+")|"+
								"("+_keywords+")|("+_lang+")|("+_link+")|("+_loop+")|("+_margin+")|"+
								"("+_muted+")|("+_onchange+")|("+_onclick+")|("+_padding+")|("+_pageicon+")|"+
								"("+_placeholder+")|("+_poster+")|("+_preload+")|("+_radiogroup+")|("+_redirect+")|"+
								"("+_selected+")|("+_sidebartype+")|("+_src+")|("+_tableattrs+")|"+
								"("+_textalign+")|("+_textcolor+")|("+_textdecoration+")|"+
								"("+_title+")|("+_tooltip+")|("+_width+"))"},			
			{__val,				"^(("+_bold+")|("+_bordered+")|("+_bottomright+")|("+_bottomleft+")|("+_bottom+")|("+_clickable+")|"+
								"("+_centered+")|("+_center+")|("+_fadein+")|("+_fading+")|("+_floating+")|("+_grayscalemin+")|"+
								"("+_grayscalemax+")|("+_grayscale+")|("+_hoverable+")|("+_italic+")|("+_left+")|"+
								"("+_movedown+")|("+_moveleft+")|("+_moveright+")|("+_moveup+")|("+_opacitymin+")|"+
								"("+_opacitymax+")|("+_opacity+")|("+_overline+")|("+_right+")|("+_sepiamin+")|"+
								"("+_sepiamax+")|("+_sepia+")|("+_sliding+")|("+_spin+")|("+_strikethrough+")|("+_striped+")|"+
								"("+_topright+")|("+_topleft+")|("+_top+")|("+_underline+")|("+_zoom+"))"},			
			{__var,				"^(\\$[a-zA-Z_][a-zA-Z0-9_-]*)"},			
			{__text,			"^(\"[^\"]*\")"},			
			{__color,			"^(\\#[0-9a-fA-F]{6})"},			
			{__integer,			"^(0|([1-9][0-9]*))"},			
			{__measure,			"^((0|[1-9][0-9]*)((px)|(%)))"},			
			{__bool,			"^(("+_true+")|("+_false+"))"}	
	};

}

