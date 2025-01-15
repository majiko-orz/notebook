package designPattern.behavioralPatterns;

/**
 * 命令模式：它将请求封装成一个对象，从而允许用不同的请求对客户端参数化，对请求排队或记录请求日志，以及支持可撤销的操作
 * 角色：命令（Command）、具体命令（Concrete Command）、调用者（Invoker）、接收者(Receiver)、客户端（Client）
 */

interface Command {
    void execute();
}

class PlayCommand implements Command {
    private Player player;

    public PlayCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.play();
    }
}

class PauseCommand implements Command {
    private Player player;

    public PauseCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.pause();
    }
}

class Player {
    public void play() {
        System.out.println("播放");
    }

    public void pause() {
        System.out.println("暂停");
    }
}

class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

class CommandClient {
    public static void main(String[] args) {
        Player player = new Player();
        Command playCommand = new PlayCommand(player);
        Command pauseCommand = new PauseCommand(player);

        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setCommand(playCommand);
        remoteControl.pressButton();

        remoteControl.setCommand(pauseCommand);
        remoteControl.pressButton();
    }
}