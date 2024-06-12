"use client";

import { format } from "date-fns";
import { pl, enUS } from "date-fns/locale";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormMessage,
} from "@/components/ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { toast } from "@/components/ui/use-toast";
import {TimePickerDemo} from "@/components/ui/time-picker-demo.tsx";
import {CalendarIcon} from "lucide-react";
import {Calendar} from "@/components/ui/calendar.tsx";
import {useTranslation} from "react-i18next";

const formSchema = z.object({
    dateTime: z.date().min(new Date(),{message:"You can't disable parking in the past"}),
});

type FormSchemaType = z.infer<typeof formSchema>;

export function DateTimePickerForm() {
    const {t} = useTranslation();
    const locale = window.navigator.language == "pl" ? pl : enUS;
    const form = useForm<FormSchemaType>({
        resolver: zodResolver(formSchema),
    });

    function onSubmit(data: FormSchemaType) {
        toast({
            title: "You submitted the following values:",
            description: (
                <pre>
          <code>{JSON.stringify(new Date(data.dateTime.valueOf() - (data.dateTime.getTimezoneOffset())), null, 2)}</code>
        </pre>
            ),
        });
    }

    return (
        <Form {...form}>
            <form
                className="flex items-end gap-4 justify-center"
                onSubmit={form.handleSubmit(onSubmit)}
            >
                <FormField
                    control={form.control}
                    name="dateTime"
                    render={({ field }) => (
                        <FormItem className="flex flex-col">
                            <Popover>
                                <FormControl>
                                    <PopoverTrigger asChild>
                                        <Button
                                            variant="outline"
                                            className={cn(
                                                "w-[280px] justify-start text-left font-normal",
                                                !field.value && "text-muted-foreground"
                                            )}
                                        >
                                            <CalendarIcon className="mr-2 h-4 w-4" />
                                            {field.value ? (
                                                format(field.value, "PPP HH:mm:ss", {locale:locale})
                                            ) : (
                                                <span>{t("general.pickDate")}</span>
                                            )}
                                        </Button>
                                    </PopoverTrigger>
                                </FormControl>
                                <FormMessage/>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        locale={locale}
                                        mode="single"
                                        selected={field.value}
                                        onSelect={field.onChange}
                                        initialFocus
                                    />
                                    <div className="p-3 border-t border-border">
                                        <TimePickerDemo
                                            setDate={field.onChange}
                                            date={field.value}
                                        />
                                    </div>
                                </PopoverContent>
                            </Popover>
                            <Button type="submit">Submit</Button>
                        </FormItem>
                    )}
                />
            </form>
        </Form>
    );
}