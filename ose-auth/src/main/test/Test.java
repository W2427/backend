import com.ose.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static Pattern NUMBER = Pattern.compile("^([\\-+]?[0-9]+(\\.[0-9]+)?)(.+)$");

    public static String toNumberFormat(String string) {

        if (!StringUtils.isEmpty(string, true)) {
            string = string
                .replaceAll("[^.\\-+0-9]+", ".")
                .replaceAll("^\\+", "")
                .replaceAll("\\.$", "");
        }

        Matcher matcher = NUMBER.matcher(string);

        if (matcher.matches()) {
            string = matcher.group(1);
        } else {
            string = "0";
        }

        return string;
    }

    public static void main(String[] args) {
        System.out.println(toNumberFormat("+99\"888'1111"));
    }

}
