# Image Measurement Tool

A Java Swing application that allows you to import an image and measure objects within it based on a known reference length. Created to help mark up images for my Fluid Mechanics final project.

## Features

- **Import Images:** Load an image (`.jpg`, `.jpeg`, `.png`, `.gif`) into the application to begin measuring.
- **Reference Measurement:** Draw a baseline (in green) between two points in the image. You will be prompted to enter the known real-world length and choose units (Inches or Centimeters).
- **Relative Measurements:** Draw subsequent lines (in red) to measure other objects in the image. Their lengths are automatically calculated relative to the baseline reference and displayed on the image.
- **Save Image:** Save the current image along with all the drawn measurement lines overlaid on it as a `.jpg` file.
- **Clear Lines:** Clear all currently drawn lines and reset the baseline to start fresh on the same image.
- **New Image:** Open a new image file and reset the workspace.
- **Adjust Line Size:** Use the "Size Up" and "Size Down" buttons to increase or decrease the thickness of the measurement lines and the size of the text labels.

## Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher.
- **Apache Maven:** For building and running the project.

## How to Build and Run

1. **Clone or Download the Repository:** Ensure you have the project files locally.
2. **Navigate to the Project Directory:** Open your terminal and navigate to the root directory containing `pom.xml`.
3. **Compile the Project:**
   ```bash
   mvn clean compile
   ```
4. **Run the Application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.benliebkemann.Main"
   ```
   *Alternatively, you can build the JAR file and run it directly:*
   ```bash
   mvn clean package
   java -jar target/measurement-1.0-SNAPSHOT.jar
   ```

## Usage Instructions

1. **Launch the Application.** You will immediately be prompted to select an image file.
2. **Set the Baseline:**
   - Click to define the start point, then move your mouse and click again to define the end point of a known reference object in the image.
   - A prompt will appear asking for the length and unit of this first line.
   - Enter the real-world value (e.g., `10`) and select whether it is in **Inches** or **Centimeters**. This baseline is drawn in **green**.
3. **Measure Other Objects:**
   - Continue clicking between points to draw additional lines. These lines will be drawn in **red**.
   - The application will automatically display the calculated measurements for these new lines based on your initial baseline.
4. **Toolbar Options:**
   - **Save:** Saves a copy of the image with the measurements visible.
   - **Clear:** Removes all measurements.
   - **New Image:** Prompts you to open a new image and resets the workspace.
   - **Size Up / Size Down:** Adjusts the thickness of the drawn lines and the font size of the measurement labels.
