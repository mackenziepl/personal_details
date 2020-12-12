package utils;

import java.util.Scanner;

public class MenuUtils {

    public static void startConsoleProgram() {
        Character option = ' ';

        while (!option.equals('4')) {
            option = menu();

            switch (option) {
                case '1':
                    XmlParserUtils.prepareDataAndSave();
                    break;
                case '2':
                    CsvParserUtils.prepareDataAndSave();
                    break;
                case '3':
                    RepositoryUtils.showDataBases();
                    break;
                case '4':
                    System.out.println("END");
                    System.out.println();
                    break;
                default:
                    System.out.println("Don't understand");
            }
        }
    }

    private static Character menu() {
        Scanner scanner = new Scanner(System.in);
        Character option = ' ';

        while (isNotAvailableChoice(option)) {
            System.out.println();
            System.out.println(" *** Menu *** ");
            System.out.println("1 - Save data from file xml");
            System.out.println("2 - Save data from file csv");
            System.out.println("3 - Write out data");
            System.out.println("4 - End");
            System.out.print("> ");
            option = scanner.next().charAt(0);
        }
        return option;
    }

    private static boolean isNotAvailableChoice(Character option) {
        return !option.equals('1') && !option.equals('2') && !option.equals('3')
                && !option.equals('4');
    }
}
