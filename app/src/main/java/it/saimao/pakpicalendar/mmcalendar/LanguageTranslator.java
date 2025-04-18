package it.saimao.pakpicalendar.mmcalendar;

/**
 * Language Translator
 *
 * @author <a href="mailto:chanmratekoko.dev@gmail.com">Chan Mrate Ko Ko</a>
 * @version 1.0.0
 * @since 1.0.7 Release
 */
public class LanguageTranslator {

    /**
     * Initialize the language catalog with 2 dimensional array
     * Index
     * <pre>
     *  0: English
     *  1: Myanmar (Unicode)
     *  2: Zawgyi
     *  3: Mon
     *  4: Tai
     *  5: Karen
     * </pre>
     * Credit:
     * Mon language translation by 'ITVilla': <a href="http://it-villa.blogspot.com/">IT Villa</a>
     * and Proof reading by Mikau Nyan
     * Tai language translation by 'Jao Tai Num'
     * and  <a href="https://www.taidictionary.com/">Tai Dictionary</a>
     */
    private static final String[][] CATALOG = {
            {"Myanmar Year", "မြန်မာနှစ်", "ျမန္မာႏွစ္", "သက္ကရာဇ်ဍုၚ်", "ပီႊၵေႃးၸႃႇ", "ကီၢ်ပယီၤ"},
            {"Good Friday", "သောကြာနေ့ကြီး", "ေသာၾကာေန႔ႀကီး", "သောကြာနေ့ကြီး", "ဢၼ်လီဝၼ်းသုၵ်း", "မုၢ်ဖီဖး"},
            {"New Year's", "နှစ်ဆန်း", "ႏွစ္ဆန္း", "လှာဲသၞာံ", "ပီႊမႂ်ႇ", "နှစ်ဆန်း"},
            {"Independence", "လွတ်လပ်ရေး", "လြတ္လပ္ေရး", "သၠးပွး", "လွတ်ႈလႅဝ်းသၢဝ်းၶေႃ", "လွတ်လပ်ရေး"},
            {"Union", "ပြည်ထောင်စု", "ျပည္ေထာင္စု", "ကၟိန်ဍုၚ်", "မိူင်းႁူမ်ႈတုမ်", "ပြည်ထောင်စု"},
            {"Peasants'", "တောင်သူလယ်သမား", "ေတာင္သူလယ္သမား", "သၟာဗ္ၚ", "ၸဝ်ႈႁႆႈၸဝ်ႈၼႃး", "တောင်သူလယ်သမား"},
            {"Resistance", "တော်လှန်ရေး", "ေတာ္လွန္ေရး", "ပၠန်ဂတးဗၟာ", "လုၵ်ႉၽိုၼ်ႉ", "တော်လှန်ရေး"},
            {"Labour", "အလုပ်သမား", "အလုပ္သမား", "သၟာကမၠောန်", "ၵူၼ်းႁဵတ်းၵၢၼ်", "အလုပ်သမား"},
            {"Martyrs'", "အာဇာနည်", "အာဇာနည္", "အာဇာနဲ", "ၽူႈႁတ်းႁၢၼ်", "အာဇာနည်"},
            {"Christmas", "ခရစ္စမတ်", "ခရစၥမတ္", "ခရေဿမာတ်", "ပွႆးၶရိတ်ႉၸမတ်ႉ", "ခရံာ်အိၣ်ဖျဲၣ်မူးပွဲန့ၣ်"},
            {"Buddha", "ဗုဒ္ဓ", "ဗုဒၶ", "သ္ဘၚ်ဖဍာ်ဇြဲ", "ႁူတ်ႉၼမ်ႉငဝ်ႈႁႆး", "ဗုဒ္ဓ"},
            {"Start of Buddhist Lent", "ဓမ္မစကြာနေ့", "ဓမၼစၾကာေန႔", "တ္ၚဲတွံဓဝ်ဓမ္မစက်", "ထမ်ႇမၸၵ်ႉၵ (ၶဝ်ႈဝႃႇ)", "ဓမ္မစကြာနေ့"},
            {"End of Buddhist Lent", "မီးထွန်းပွဲ", "မီးထြန္းပြဲ", "တ္ၚဲအဘိဓရ်", "ဝၼ်းဢၽိထမ်ႇမႃႇ (ဢွၵ်ႇဝႃႇ)", "မီးထွန်းပွဲ"},
            {"Tazaungdaing", "တန်ဆောင်တိုင်", "တန္ေဆာင္တိုင္", "သ္ဘၚ်ပူဇဴပၟတ်ပၞာၚ်", "ပွႆးတႆႈတဵၼ်းႁဵင်", "တန်ဆောင်တိုင်"},
            {"National", "အမျိုးသား", "အမ်ိဳးသား", "ကောန်ဂကူဗၟာ", "ၶိူဝ်းမိူင်း", "အမျိုးသား"},
            {"Karen", "ကရင်", "ကရင္", "ကရေၚ်", "ယၢင်းၽိူၵ်ႇ", "ကရင်"},
            {"Pwe", "ပွဲ", "ပြဲ", "သ္ဘၚ်", "ပွႆး", "ပွဲ"},
            {"Thingyan", "သင်္ကြန်", "သၾကၤန္", "အတး", "သၢင်းၵျၢၼ်ႇ", "သင်္ကြန်"},
            {"Akyo", "အကြို", "အႀကိဳ", "ဒစး", "အကြို", "ႁပ်ႉ"},
            {"Akyat", "အကြတ်", "အၾကတ္", "ကြာပ်", "ၵျၢပ်ႈ", "အကြတ်"},
            {"Akya", "အကျ", "အက်", "စှေ်", "တူၵ်း", "အကျ"},
            {"Atat", "အတက်", "အတက္", "တိုန်", "ၶိုၼ်ႈ", "အတက်"},
            {"Amyeittasote", "အမြိတ္တစုတ်", "အၿမိတၱစုတ္", "ကိုန်အမြိုတ်", "အမြိတ္တစုတ်", "အမြိတ္တစုတ်"},
            {"Warameittugyi", "ဝါရမိတ္တုကြီး", "ဝါရမိတၱဳႀကီး", "ကိုန်ဝါရမိတ္တုဇၞော်", "ဝါရမိတ္တုကြီး", "ဝါရမိတ္တုကြီး"},
            {"Warameittunge", "ဝါရမိတ္တုငယ်", "ဝါရမိတၱဳငယ္", "ကိုန်ဝါရမိတ္တုဍောတ်", "ဝါရမိတ္တုငယ်", "ဝါရမိတ္တုငယ်"},
            {"Thamaphyu", "သမားဖြူ", "သမားျဖဴ", "ကိုန်လေၚ်ဒိုက်", "သမားဖြူ", "သမားဖြူ"},
            {"Thamanyo", "သမားညို", "သမားညိဳ", "ကိုန်ဟုံဗြမ်", "သမားညို", "သမားညို"},
            {"Yatpote", "ရက်ပုပ်", "ရက္ပုပ္", "ကိုန်လီုလာ်", "ရက်ပုပ်", "ရက်ပုပ်"},
            {"Yatyotema", "ရက်ယုတ်မာ", "ရက္ယုတ္မာ", "ကိုန်ယုတ်မာ", "ရက်ယုတ်မာ", "ရက်ယုတ်မာ"},
            {"Mahayatkyan", "မဟာရက်ကြမ်း", "မဟာရက္ၾကမ္း", "ကိုန်ဟွံခိုဟ်", "မဟာရက်ကြမ်း", "မဟာရက်ကြမ်း"},
            {"Nagapor", "နဂါးပေါ်", "နဂါးေပၚ", "နာ်မံက်", "နဂါးပေါ်", "နဂါးပေါ်"},
            {"Shanyat", "ရှမ်းရက်", "ရွမ္းရက္", "တ္ၚဲဒတန်", "ရှမ်းရက်", "ရှမ်းရက်"},
            {"Mooon", "မွန်", "မြန္", "ပၠန်", "မွၼ်း", "မွန်"},
            {"G. Aung San BD", "ဗိုလ်ချုပ်မွေးနေ့", "ဗိုလ္ခ်ဳပ္ေမြးေန႔", "တ္ၚဲသၟိၚ်ဗၟာ အံၚ်သာန်ဒှ်မၞိဟ်", "ဝၼ်းၵိူတ်ၸွမ်သိုၵ်", "ဗိုလ်ချုပ်မွေးနေ့"},
            {"Valentines", "ချစ်သူများ", "ခ်စ္သူမ်ား", "ဝုတ်ဗၠာဲ", "ၵေႃႈႁၵ်ႉ", "ချစ်သူများ"},
            {"Earth", "ကမ္ဘာမြေ", "ကမၻာေျမ", "ဂၠးကဝ်", "လိၼ်မိူင်း", "ကမ္ဘာမြေ"},
            {"April Fools'", "ဧပြီအရူး", "ဧၿပီအ႐ူး", "သ္ပပရအ်", "ဢေႇပရႄႇၵူၼ်းယွင်ႇ", "အ့ဖြ့ၣ် fool"},
            {"Red Cross", "ကြက်ခြေနီ", "ၾကက္ေျခနီ", "ဇိုၚ်ခ္ဍာ်ဍာဲ", "ဢၼ်မီးသီလႅင်ႁၢင်ႈၶႂၢႆႇၶႃပေ", "ကြက်ခြေနီ"},
            {"United Nations", "ကုလသမ္မဂ္ဂ", "ကုလသမၼဂၢ", "ကုလသမ္မဂ္ဂ", "ဢၼ်ၽွမ်ႉႁူမ်ႈၸိူဝ်ႉၶိူဝ်းၼမ်", "ကုလသမ္မဂ္ဂ"},
            {"Halloween", "သရဲနေ့", "သရဲေန႔", "ဟေဝ်လဝ်ဝိန်", "ဝၼ်းၽဵတ်", "သရဲနေ့"},
            {"Shan", "ရှမ်း", "ရွမ္း", "သေံ", "တႆး", "ရှမ်း"},
            {"Mothers'", "အမေများ", "အေမမ်ား", "မိအံက်", "မႄႈ", "မိၢ်အ"},
            {"Fathers'", "အဖေများ", "အေဖမ်ား", "မအံက်", "ပေႃ", "ပၢ်အ"},
            {"Sasana Year", "သာသနာနှစ်", "သာသနာႏွစ္", "သက္ကရာဇ်သာသနာ", "ပီႊသႃႇသၼႃႇ", "နံၣ်သာသနာ"},
            {"Eid", "အိဒ်", "အိဒ္", "အိဒ်", "ဢိတ်ႉ", "အိဒ်"},
            {"Diwali", "ဒီဝါလီ", "ဒီဝါလီ", "ဒီဝါလီ", "တီႇဝႃႇလီႇ", "ဒီဝါလီ"},
            {"Mahathamaya", "မဟာသမယ", "မဟာသမယ", "မဟာသမယ", "ဢၼ်ယႂ်ႇၽွမ်ႉႁူမ်ႈ", "မဟာသမယ"},
            {"Garudhamma", "ဂရုဓမ္မ", "ဂ႐ုဓမၼ", "ဂရုဓမ္မ", "ဂရုဓမ္မ", "ဂရုဓမ္မ"},
            {"Metta", "မေတ္တာ", "ေမတၱာ", "မေတ္တာ", "မႅတ်ႉတႃႇ", "မေတ္တာ"},
            {"Taungpyone", "တောင်ပြုန်း", "ေတာင္ျပဳန္း", "တောင်ပြုန်း", "တွင်ႇပျူင်း", "တောင်ပြုန်း"},
            {"Yadanagu", "ရတနာ့ဂူ", "ရတနာ့ဂူ", "ရတနာ့ဂူ", "ရတနာ့ဂူ", "ရတနာ့ဂူ"},
            {"Authors'", "စာဆိုတော်", "စာဆိုေတာ္", "စာဆိုတော်", "ၽူႈတႅမ်ႈၽႅၼ်", "စာဆိုတော်"},
            {"World", "ကမ္ဘာ့", "ကမၻာ့", "ကမ္ဘာ့", "လူၵ်", "ကမ္ဘာ့"},
            {"Teachers'", "ဆရာများ", "ဆရာမ်ား", "ဆရာများ", "ၶူးသွၼ်", "ဆရာများ"},
            {"Holiday", "ရုံးပိတ်ရက်", "႐ုံးပိတ္ရက္", "ရုံးပိတ်ရက်", "ဝၼ်းပိၵ်ႉလုမ်း", "ရုံးပိတ်ရက်"},
            {"Chinese", "တရုတ်", "တ႐ုတ္", "တရုတ်", "ၵူၼ်းၸၢဝ်းၶေ", "တရုတ်"},
            {"Easter", "ထမြောက်ရာနေ့", "ထေျမာက္ရာေန႔", "ထမြောက်ရာနေ့", "ပၢင်ႇပွႆးၶွပ်ႈၶူပ်ႇၸဝ်ႈၶရိတ်", "ထမြောက်ရာနေ့"},
            {"0", "၀", "၀", "၀", "၀", "၀"},
            {"1", "၁", "၁", "၁", "၁", "၁"},
            {"2", "၂", "၂", "၂", "၂", "၂"},
            {"3", "၃", "၃", "၃", "၃", "၃"},
            {"4", "၄", "၄", "၄", "၄", "၄"},
            {"5", "၅", "၅", "၅", "၅", "၅"},
            {"6", "၆", "၆", "၆", "၆", "၆"},
            {"7", "၇", "၇", "၇", "၇", "၇"},
            {"8", "၈", "၈", "၈", "၈", "၈"},
            {"9", "၉", "၉", "၉", "၉", "၉"},
            {"Sunday", "တနင်္ဂနွေ", "တနဂၤေႏြ", "တ္ၚဲအဒိုတ်", "ဢႃးတိတ်ႉ", "မုၢ်ဒဲး"},
            {"Monday", "တနင်္လာ", "တနလၤာ", "တ္ၚဲစန်", "ၸၼ်", "မုၢ်ဆၣ်"},
            {"Tuesday", "အင်္ဂါ", "အဂၤါ", "တ္ၚဲအင္ၚာ", "ဢင်းၵၢၼ်း", "မုၢ်ယူာ်"},
            {"Wednesday", "ဗုဒ္ဓဟူး", "ဗုဒၶဟူး", "တ္ၚဲဗုဒ္ဓဝါ", "ပုတ်ႉ", "မုၢ်ပျဲၤ"},
            {"Thursday", "ကြာသပတေး", "ၾကာသပေတး", "တ္ၚဲဗြဴဗတိ", "ၽတ်း", "မုၢ်လ့ၤဧိၤ"},
            {"Friday", "သောကြာ", "ေသာၾကာ", "တ္ၚဲသိုက်", "သုၵ်း", "မုၢ်ဖီဖး"},
            {"Saturday", "စနေ", "စေန", "တ္ၚဲသ္ၚိသဝ်", "သဝ်", "မုၢ်ဘူၣ်"},
            {"Sabbath Eve", "အဖိတ်", "အဖိတ္", "တ္ၚဲတိၚ်", "ၽိတ်ႈ", "အဖိတ်"},
            {"Sabbath", "ဥပုသ်", "ဥပုသ္", "တ္ၚဲသဳ", "သိၼ်", "အိၣ်ဘှံး"},
            {"Yatyaza", "ရက်ရာဇာ", "ရက္ရာဇာ", "တ္ၚဲရာဇာ", "ဝၼ်းထုၼ်း", "ရက်ရာဇာ"},
            {"Pyathada", "ပြဿဒါး", "ျပႆဒါး", "တ္ၚဲပြာဗ္ဗဒါ", "ဝၼ်းပျၢတ်ႈ", "ပြဿဒါး"},
            {"Afternoon", "မွန်းလွဲ", "မြန္းလြဲ", "မွန်းလွဲ", "ဝၢႆးဝၼ်း", "မွန်းလွဲ"},
            {"January", "ဇန်နဝါရီ", "ဇန္နဝါရီ", "ဂျာန်နျူအာရဳ", "ၸၼ်ႇဝႃႇရီႇ", "ယနူၤအါရံၤ"},
            {"February", "ဖေဖော်ဝါရီ", "ေဖေဖာ္ဝါရီ", "ဝှေဝ်ဗျူအာရဳ", "ၾႅပ်ႉဝႃႇရီႇ", "ဖ့ၤဘြူၤအါရံၤ"},
            {"March", "မတ်", "မတ္", "မာတ်ချ်", "မျၢတ်ႉၶျ်", "မၢ်ၡး"},
            {"April", "ဧပြီ", "ဧၿပီ", "ဨပြေယ်လ်", "ဢေႇပရႄႇ", "အ့ဖြ့ၣ်"},
            {"May", "မေ", "ေမ", "မေ", "မေႇ", "မ့ၤ"},
            {"June", "ဇွန်", "ဇြန္", "ဂျုန်", "ၵျုၼ်ႇ", "ယူၤ"},
            {"July", "ဇူလိုင်", "ဇူလိုင္", "ဂျူလာၚ်", "ၵျူႇလၢႆႇ", "ယူၤလံ"},
            {"August", "ဩဂုတ်", "ဩဂုတ္", "အဝ်ဂါတ်", "ဢေႃးၵၢတ်ႉ", "အီကူး"},
            {"September", "စက်တင်ဘာ", "စက္တင္ဘာ", "သိတ်ထီဗာ", "သႅပ်ႇထႅမ်ႇပႃႇ", "စဲးပတ့ဘၢၣ်"},
            {"October", "အောက်တိုဘာ", "ေအာက္တိုဘာ", "အံက်ထဝ်ဗာ", "ဢွၵ်ႇထူဝ်ႇပႃႇ", "အီးကထိဘၢၣ်"},
            {"November", "နိုဝင်ဘာ", "နိုဝင္ဘာ", "နဝ်ဝါမ်ဗာ", "ၼူဝ်ႇဝႅမ်ႇပႃႇ", "နိၣ်ဝ့ဘၢၣ်"},
            {"December", "ဒီဇင်ဘာ", "ဒီဇင္ဘာ", "ဒီဇြေန်ဗာ", "တီႇသႅမ်ႇပႃႇ", "ဒံၣ်စ့ဘၢၣ်"},
            {"Tagu", "တန်ခူး", "တန္ခူး", "ဂိတုစဲ", "ႁႃႈ", "လါချံ"},
            {"Kason", "ကဆုန်", "ကဆုန္", "ဂိတုပသာ်", "ႁူၵ်း", "ဒ့ၣ်ညါ"},
            {"Nayon", "နယုန်", "နယုန္", "ဂိတုဇှေ်", "ၸဵတ်း", "လါနွံ"},
            {"Waso", "ဝါဆို", "ဝါဆို", "ဂိတုဒ္ဂိုန်", "ပႅတ်ႇ", "လါဃိး"},
            {"Wagaung", "ဝါခေါင်", "ဝါေခါင္", "ဂိတုခ္ဍဲသဳ", "ၵဝ်ႈ", "လါခူး"},
            {"Tawthalin", "တော်သလင်း", "ေတာ္သလင္း", "ဂိတုဘတ်", "သိပ်း", "ဆံးမုၢ်"},
            {"Thadingyut", "သီတင်းကျွတ်", "သီတင္းကြ်တ္", "ဂိတုဝှ်", "သိပ်းဢဵတ်း", "ဆံးဆၣ်"},
            {"Tazaungmon", "တန်ဆောင်မုန်း", "တန္ေဆာင္မုန္း", "ဂိတုက္ထိုန်", "သိပ်းသွင်", "လါနီ"},
            {"Nadaw", "နတ်တော်", "နတ္ေတာ္", "ဂိတုမြေက္ကသဵု", "ၸဵင်", "လါပျုၤ"},
            {"Pyatho", "ပြာသို", "ျပာသို", "ဂိတုပှော်", "ၵမ်", "သလ့ၤ"},
            {"Tabodwe", "တပို့တွဲ", "တပို႔တြဲ", "ဂိတုမာ်", "သၢမ်", "ထ့ကူး"},
            {"Tabaung", "တပေါင်း", "တေပါင္း", "ဂိတုဖဝ်ရဂိုန်", "သီႇ", "သွ့ကီ"},
            {"First", "ပ", "ပ", "ပ", "ပ", "၁ "},
            {"Second", "ဒု", "ဒု", "ဒု", "တု", "၂ "},
            {"First Waso", "", "", "", "ပ ပႅတ်ႇ", ""},
            {"Second Waso", "", "", "", "တု ပႅတ်ႇ", ""},
            {"Late", "နှောင်း", "ေႏွာင္း", "နှောင်း", "ဝၢႆး", "စဲၤ"},
            {"Late Tagu", "နှောင်းတန်ခူး", "ေႏွာင္းတန္ခူး", "နှောင်းဂိတုစဲ", "ႁႃႈ", "စဲၤလါချံ"},
            {"Late Kason", "နှောင်းကဆုန်", "ေႏွာင္းကဆုန္", "နှောင်းဂိတုပသာ်", "ႁူၵ်း", "စဲၤဒ့ၣ်ညါ"},
            {"Waxing", "လဆန်း", "လဆန္း", "မံက်", "မႂ်ႇ", "လါထီၣ်"},
            {"Waning", "လဆုတ်", "လဆုတ္", "စွေက်", "လွင်ႈ", "လါလီၤ"},
            {"Full Moon", "လပြည့်", "လျပည့္", "ပေၚ်", "မူၼ်း", "လါပှဲၤ"},
            {"New Moon", "လကွယ်", "လကြယ္", "အိုတ်", "လပ်း", "လါဘၢ"},
            {"Nay", "နေ့", "ေန႔", "တ္ၚဲ", "ဝၼ်း", "နံၤ"},
            {"Day", "နေ့", "ေန႔", "တ္ၚဲ", "ဝၼ်း", "နံၤ"},
            {"Yat", "ရက်", "ရက္", "ရက်", "ဝၼ်း", "ရက်"},
            {"Year", "နှစ်", "ႏွစ္", "နှစ်", "ပီ", "နံၣ်"},
            {"Ku", "ခု", "ခု", "သၞာံ", "ၶု", ""},
            {"Naga", "နဂါး", "နဂါး", "နဂါး", "ႁူဝ်", "နဂါး"},
            {"Head", "ခေါင်း", "ေခါင္း", "ခေါင်း", "ၼၵႃး", "ခေါင်း"},
            {"Facing", "လှည့်", "လွည့္", "လှည့်", "ဝၢႆႇ", "လှည့်"},
            {"East", "အရှေ့", "အေရွ႕", "အရှေ့", "တၢင်းဢွၵ်ႇ", "အရှေ့"},
            {"West", "အနောက်", "အေနာက္", "အနောက်", "တၢင်းတူၵ်း", "အနောက်"},
            {"South", "တောင်", "ေတာင္", "တောင်", "တၢင်းၸၢၼ်း", "တောင်"},
            {"North", "မြောက်", "ေျမာက္", "မြောက်", "တၢင်းႁွင်ႇ", "မြောက်"},
            {"Mahabote", "မဟာဘုတ်", "မဟာဘုတ္", "မဟာဘုတ်", "မဟာဘုတ်", "မဟာဘုတ်"},
            {"Born", "ဖွား", "ဖြား", "ဖွား", "ဢၼ်မီးပႃႇရမီ", "ဖွား"},
            {"Binga", "ဘင်္ဂ", "ဘဂၤ", "ဘင်္ဂ", "ဘင်္ဂ", "ဘင်္ဂ"},
            {"Atun", "အထွန်း", "အထြန္း", "အထွန်း", "အထွန်း", "အထွန်း"},
            {"Yaza", "ရာဇ", "ရာဇ", "ရာဇ", "ရာဇ", "ရာဇ"},
            {"Adipati", "အဓိပတိ", "အဓိပတိ", "အဓိပတိ", "အဓိပတိ", "အဓိပတိ"},
            {"Marana", "မရဏ", "မရဏ", "မရဏ", "မရဏ", "မရဏ"},
            {"Thike", "သိုက်", "သိုက္", "သိုက်", "သိုက်", "သိုက်"},
            {"Puti", "ပုတိ", "ပုတိ", "ပုတိ", "ပုတိ", "ပုတိ"},
            {"Ogre", "ဘီလူး", "ဘီလူး", "ဘီလူး", "ၽီလူ", "ဘီလူး"},
            {"Elf", "နတ်", "နတ္", "နတ်", "ၽီမႅၼ်းဢွၼ်", "နတ်"},
            {"Human", "လူ", "လူ", "လူ", "ဢၼ်ပဵၼ်ၵူၼ်", "လူ"},
            {"Nakhat", "နက္ခတ်", "နကၡတ္", "နက္ခတ်", "လႅင်ႊလၢဝ်", "နက္ခတ်"},
            {"Hpusha", "ပုဿ", "ပုႆ", "ပုဿ", "ပုဿ", "ပုဿ"},
            {"Magha", "မာခ", "မာခ", "မာခ", "မာခ", "မာခ"},
            {"Phalguni", "ဖ္လကိုန်", "ဖႅကိုန္", "ဖ္လကိုန်", "ဖ္လကိုန်", "ဖ္လကိုန်"},
            {"Chitra", "စယ်", "စယ္", "စယ်", "စယ်", "စယ်"},
            {"Visakha", "ပိသျက်", "ပိသ်က္", "ပိသျက်", "ပိသျက်", "ပိသျက်"},
            {"Jyeshtha", "စိဿ", "စိႆ", "စိဿ", "စိဿ", "စိဿ"},
            {"Ashadha", "အာသတ်", "အာသတ္", "အာသတ်", "အာသတ်", "အာသတ်"},
            {"Sravana", "သရဝန်", "သရဝန္", "သရဝန်", "သရဝန်", "သရဝန်"},
            {"Bhadrapaha", "ဘဒြ", "ဘျဒ", "ဘဒြ", "ဘဒြ", "ဘဒြ"},
            {"Asvini", "အာသိန်", "အာသိန္", "အာသိန်", "အာသိန်", "အာသိန်"},
            {"Krittika", "ကြတိုက်", "ၾကတိုက္", "ကြတိုက်", "ကြတိုက်", "ကြတိုက်"},
            {"Mrigasiras", "မြိက္ကသိုဝ်", "ၿမိကၠသိုဝ္", "မြိက္ကသိုဝ်", "မြိက္ကသိုဝ်", "မြိက္ကသိုဝ်"},
            {"Calculator", "တွက်စက်", "တြက္စက္", "တွက်စက်", "သွၼ်", "တွက်စက်"},
            //{". ","။ ","။ ","။ ","။ ","။ "},
            //{", ","၊ ","၊ ","၊ ","၊ ","၊ "},
    };


    private LanguageTranslator() {
    }

    /**
     * Translate language
     * <br/
     * Language number:
     * <ol start="0">
     *  <li>English</li>
     *  <li>Myanmar (Unicode)</li>
     *  <li>Zawgyi</li>
     *  <li>Mon</li>
     *  <li>Tai</li>
     *  <li>Karen</li>
     * </ol>
     *
     * @param str    string to translate
     * @param toLn   to language number
     * @param fromLn from language number
     * @return Translated string
     */
    private static String translateSentence(String str, int fromLn, int toLn) {
        for (String[] dic : CATALOG) {
            str = str.replace(dic[fromLn], dic[toLn]);
        }
        return str;
    }

    /**
     * Translate language
     * <br/
     * Language number:
     * <ol start="0">
     *  <li>English</li>
     *  <li>Myanmar (Unicode)</li>
     *  <li>Zawgyi</li>
     *  <li>Mon</li>
     *  <li>Tai</li>
     *  <li>Karen</li>
     * </ol>
     *
     * @param str    string to translate
     * @param toLn   to language number
     * @param fromLn from language number
     * @return Translated string
     */
    private static String translate(String str, int fromLn, int toLn) {
        for (String[] dic : CATALOG) {
            if (dic[fromLn].equals(str)) {
                return dic[toLn];
            }
        }
        return str;
    }

    /**
     * Translate sentence to the specific language
     *
     * @param str  the string to translate
     * @param from Translate Language from
     * @param to   Translate Language to
     * @return translated result
     */
    public static String translateSentence(String str, Language from, Language to) {
        return translateSentence(str, from.getLanguageIndex(), to.getLanguageIndex());
    }

    /**
     * Translates to the specific language
     *
     * @param str  the string to translate
     * @param from Translate Language from
     * @param to   Translate Language to
     * @return translated result
     */
    public static String translate(String str, Language from, Language to) {
        return translate(str, from.getLanguageIndex(), to.getLanguageIndex());
    }

    /**
     * Translates to the specific language
     *
     * @param str the string to translate
     * @param to  Translate Language to
     * @return translated result
     */
    public static String translate(String str, Language to) {
        return translate(str, Language.ENGLISH.getLanguageIndex(), to.getLanguageIndex());
    }


    /**
     * Number to String
     *
     * @param number   Number
     * @param language LanguageCatalog object
     * @return String
     */
    public static String translate(double number, Language language) {
        if (number < 0) {
            return "-" + translate(Math.abs(number), language);
        }

        StringBuilder result = new StringBuilder();
        int digit;
        if (number == 0) {
            return "၀";
        }
        while (number > 0) {
            digit = (int) (number % 10);
            number = Math.floor(number / 10);
            result.insert(0, translate(Integer.toString(digit), language));
        }

        return result.toString();
    }

}

