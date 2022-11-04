package com.github.andy.elva.model.renderer;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.Consumer;

public interface Renderer extends Consumer<PrintWriter> {

    void accept(PrintWriter out);
}
