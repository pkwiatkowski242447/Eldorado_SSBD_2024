import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Button} from "@/components/ui/button.tsx";
import {z} from "zod";
import {useToast} from "@/components/ui/use-toast.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";

const formSchema = z.object({
    email: z.string().min(1, {message: "This field has to be filled."})
        .email("This is not a valid email."),
})

function ForgotPasswordPage() {
    const {toast} = useToast()

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        console.table(values)
        api.forgotPassword(values.email)
            .then(() => {
                toast({
                    title: "Almost there!",
                    description: "Check the e-mail you provided in order to reset the password" +
                        "to your Eldorado account.",
                });
            })
            .catch((error) => {
                toast({
                    variant: "destructive",
                    description: "Something went wrong. Please try again later.",
                })
                console.log(error.response.data)
            });
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="h-auto w-1/2"/>
            <Card className="mx-auto max-w-2xl">
                <CardHeader>
                    <CardTitle>Forgot Password</CardTitle>
                    <CardDescription>Press the button below to send an e-mail with a password reset
                        link</CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)}>
                            <div className="flex flex-col space-y-4">
                                <div>
                                    <FormField
                                        control={form.control}
                                        name="email"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">E-mail</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="mail@example.com" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <Button type="submit">Send reset link</Button>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    );
}

export default ForgotPasswordPage;