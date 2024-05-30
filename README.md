# PPanel - Change Frame Border

PPanel is a powerful tool designed to enhance your Processing sketches by allowing you to customize the frame border. It comes with pre-installed themes for the frame border, including MacOS frames, Windows frames, and the option to create your own custom themes.

## How to Install PPanel

To install PPanel, follow these steps:

1. Download PPanel from the `Releases` section.
2. The default release package includes the following components: `PPanel`, `BasePlane`, `ModernPlane`, `MacOSPlane`, and `Win95Plane`.
3. After downloading, import the library as a `.zip` archive into your Processing environment.

## How to Create Your Own Theme

Creating your own theme in PPanel involves several steps:

1. **Create a New File**: Start by creating a new file with a `.java` extension.
2. **Define the Class**: In this file, create a `public class` with the same name as the file.
3. **Extend a Base Class**: This class should extend `krcsn.ppanel.panels.BasePlane` or any other panel class from the `panels` package.
4. **Override Necessary Functions**: Override the necessary functions and implement your custom logic for the theme.
5. **Instantiate the Class**: In your main Processing sketch, instantiate the new class.
6. **Debug and Test**: Check for any bugs and fix them as needed.

Congratulations! You have successfully created your custom theme.

## Timelines

PPanel also includes a feature called Timelines, which allows you to create various animations for custom panels or any other creative ideas. Here’s how to work with Timelines:

1. **TimelineView**: Use `TimelineView` to visualize how the timeline will be played.
2. **Timeline**: Use `Timeline` to control the playback of animations. A `Timeline` consists of two or more keyframes, each with a specific time, value, and ease type.
3. **Adjust Playback**: Within a `Timeline`, you can adjust the speed of playback, specify the duration, and determine whether the `Timeline` should repeat after it ends.

For more detailed information, refer to the Javadoc for `krcsn.ppanel.Timeline`, `krcsn.ppanel.TimelineView`, and `krcsn.ppanel.Easing`.

By following these steps, you can leverage PPanel to its fullest potential, creating customized and animated frame borders for your Processing sketches.
