package machine;
import java.util.*;

enum CoffeeTypes {
    ESPRESSO( 250, 0, 16, 4, 1),
    LATTE( 350, 75, 20, 7, 1),
    CAPPUCCINO(200, 100, 12, 6, 1);

    private final int water;
    private final int milk;
    private final int coffeeBeans;
    private final int money;
    private final int cups;

    CoffeeTypes(int water, int milk, int coffeeBeans, int money, int cups) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.money = money;
        this.cups = cups;
    }

    public int getWater() {
        return water;
    }

    public int getMilk() {
        return milk;
    }

    public int getCoffeeBeans() {
        return coffeeBeans;
    }

    public int getMoney() {
        return money;
    }

    public int getCups() {
        return cups;
    }
}

enum States {
    RUNNING,
    OFF,
    BUYING_COFFEE,
    FILLING_SUPPLIES,
    TAKING_MONEY,
    DISPLAYING_REMAINING
}


class CoffeeMaker {

    private States currentState;
    private CoffeeTypes currentTypeOfCoffee;
    // Current coffee machine's supplies
    private final HashMap<String, Integer> supplies = new HashMap<>();

       public CoffeeMaker(States currentState) {
           supplies.put("water", 400);
           supplies.put("milk", 540);
           supplies.put("coffeeBeans", 120);
           supplies.put("cups", 9);
           supplies.put("money", 550);

           this.currentState = currentState;
       }

    public HashMap<String, Integer> getSupplies() {
           return supplies;
    }

    public States getCurrentState() {
           return this.currentState;
    }

    public void setState(States currentState) {
           this.currentState = currentState;
    }

    public CoffeeTypes getCurrentTypeOfCoffee() {
           return this.currentTypeOfCoffee;
    }

    public void setCurrentTypeOfCoffee(CoffeeTypes currentType) {
           this.currentTypeOfCoffee = currentType;
    }

    public void makeCoffee(HashMap<String, Integer> supplies,
                                  CoffeeTypes currentTypeOfCoffee) {
           if (isEnoughSupplies(supplies, currentTypeOfCoffee)) {
               System.out.println("I have enough resources, making you a coffee!");
               System.out.println();
               refreshSupplies(supplies, currentTypeOfCoffee);
           } else {
               System.out.println("Not enough resources.");
               System.out.println();
           }
    }

    public boolean isEnoughSupplies(HashMap<String, Integer> supplies,
                                    CoffeeTypes currentTypeOfCoffee) {
        return supplies.get("water") >= currentTypeOfCoffee.getWater()
                && supplies.get("milk") >= currentTypeOfCoffee.getMilk()
                && supplies.get("coffeeBeans") >= currentTypeOfCoffee.getCoffeeBeans()
                && supplies.get("cups") >= currentTypeOfCoffee.getCups();
    }

    public void refreshSupplies(HashMap<String, Integer> supplies,
                                       CoffeeTypes currentTypeOfCoffee) {
        supplies.replace("water", supplies.get("water")
                - currentTypeOfCoffee.getWater());
        supplies.replace("milk", supplies.get("milk")
                - currentTypeOfCoffee.getMilk());
        supplies.replace("coffeeBeans", supplies.get("coffeeBeans")
                - currentTypeOfCoffee.getCoffeeBeans());
        supplies.replace("cups", supplies.get("cups")
                - currentTypeOfCoffee.getCups());
        supplies.replace("money", supplies.get("money")
                + currentTypeOfCoffee.getMoney());
    }

    public void addSupplies(HashMap<String, Integer> supplies, int[] extraSupplies) {
        supplies.replace("water", supplies.get("water") + extraSupplies[0]);
        supplies.replace("milk", supplies.get("milk") + extraSupplies[1]);
        supplies.replace("coffeeBeans", supplies.get("coffeeBeans") + extraSupplies[2]);
        supplies.replace("cups", supplies.get("cups") + extraSupplies[3]);
    }

    public void takeMoney(HashMap<String, Integer> supplies) {
        System.out.println("I gave you $" + supplies.get("money"));
        supplies.replace("money", 0);
    }

}

public class CoffeeMachine {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CoffeeMaker coffeeMachine = new CoffeeMaker(States.RUNNING);

        while (coffeeMachine.getCurrentState() != States.OFF) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.next();

            switch (action) {
                case "buy":
                    coffeeMachine.setState(States.BUYING_COFFEE);
                    String typeOfCoffee = chooseTypeOfCoffee(scanner);

                    if (typeOfCoffee.equals("back")) {
                        break;
                    }

                    switch (typeOfCoffee) {
                        case "1":
                            coffeeMachine.setCurrentTypeOfCoffee(CoffeeTypes.ESPRESSO);
                            break;
                        case "2":
                            coffeeMachine.setCurrentTypeOfCoffee(CoffeeTypes.LATTE);
                            break;
                        case "3":
                            coffeeMachine.setCurrentTypeOfCoffee(CoffeeTypes.CAPPUCCINO);
                            break;
                        default:
                            System.out.println("Unknown type of coffee.");
                            break;
                    }

                    coffeeMachine.makeCoffee(coffeeMachine.getSupplies(),
                            coffeeMachine.getCurrentTypeOfCoffee());
                    break;
                case "fill":
                    coffeeMachine.setState(States.FILLING_SUPPLIES);
                    coffeeMachine.addSupplies(coffeeMachine.getSupplies(), fill(scanner));
                    System.out.println();
                    break;
                case "take":
                    System.out.println();
                    coffeeMachine.setState(States.TAKING_MONEY);
                    coffeeMachine.takeMoney(coffeeMachine.getSupplies());
                    System.out.println();
                    break;
                case "remaining":
                    coffeeMachine.setState(States.DISPLAYING_REMAINING);
                    displaySupplies(coffeeMachine.getSupplies());
                    break;
                case "exit":
                    coffeeMachine.setState(States.OFF);
                    break;
                default:
                    System.out.println("Unknown action.");
            }
        }
    }

    public static void displaySupplies(HashMap<String, Integer> supplies) {
        System.out.println();
        System.out.println("The coffee machine has:");
        System.out.println(supplies.get("water") + " of water");
        System.out.println(supplies.get("milk") + " of milk");
        System.out.println(supplies.get("coffeeBeans") + " of coffee beans");
        System.out.println(supplies.get("cups") + " of disposable cups");
        System.out.println("$" + supplies.get("money") + " of money");
        System.out.println();
    }

    public static String chooseTypeOfCoffee(Scanner scanner) {
        System.out.println();
        System.out.println("What do you want to buy? " +
                "1 - espresso, " +
                "2 - latter, " +
                "3 - cappuccino, " +
                "back - to main menu:");

        return scanner.next();
    }

    public static int[] fill(Scanner scanner) {
        int[] extraSupplies = new int[4];

        System.out.println();

        System.out.println("Write how many ml of water do you want to add:");
        extraSupplies[0] = scanner.nextInt();

        System.out.println("Write how many ml of milk do you want to add:");
        extraSupplies[1] = scanner.nextInt();

        System.out.println("Write how many grams of coffee beans do you want to add:");
        extraSupplies[2] = scanner.nextInt();

        System.out.println("Write how many disposable cups of coffee do you want to add:");
        extraSupplies[3] = scanner.nextInt();

        return extraSupplies;
    }

}
